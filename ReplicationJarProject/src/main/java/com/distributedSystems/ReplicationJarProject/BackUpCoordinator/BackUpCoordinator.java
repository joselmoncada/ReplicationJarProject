package com.distributedSystems.ReplicationJarProject.BackUpCoordinator;

import com.distributedSystems.ReplicationJarProject.Producer.BackUpRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackUpCoordinator {


    private ServerSocket serverSocket = null;


    public BackUpCoordinator(){


    }


    public static void main(String[] args) {

        new BackUpCoordinator().startServer(4444);
    }



    public void startServer(int port) {

        try {
            System.out.println("SERVIDOR ESPERANDO CLIENTES EN PUERTO: "+port);
            serverSocket = new ServerSocket(port);

            while (true){

                Socket clientSocket = serverSocket.accept();
                BackUpCoordinatorClientHandler clientHandler= new BackUpCoordinatorClientHandler(clientSocket);
                clientHandler.start();


            }

        } catch (IOException e) {
            System.out.println("Error: "+e);
            e.printStackTrace();
        }

    }



    private class BackUpCoordinatorClientHandler extends Thread {
        private Socket clientSocket;
        public ObjectInputStream in;
        public ObjectOutputStream out;
        private  FileWriter file;
        private  FileReader reader;

        public BackUpCoordinatorClientHandler(Socket socket) {
            this.clientSocket = socket;
            System.out.println("Server accepted new client, Thread: "+Thread.currentThread().getId());

        }

        public StateRegister loadStateJSON() {
            JSONParser parser = new JSONParser();
            JSONArray stateList = new JSONArray();
            StateRegister temporary = new StateRegister();
            System.out.println("Loading states...");

            // Reading the JSON and getting the States
            try {
                reader = new FileReader("states.json");
                Object object = parser.parse(reader);
                stateList = (JSONArray) object;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Convert the last State
            try {
                JSONObject o = (JSONObject) stateList.get(stateList.size() - 1);
                temporary.setProductA(Integer.valueOf(o.get("productA").toString()));
                temporary.setProductB(Integer.valueOf(o.get("productB").toString()));
            } catch (Exception e) { // Gets here if stateList is empty
                e.printStackTrace();
            }

            return temporary;
        }

        public void saveStateJSON(BackUpRequest request) throws RemoteException {
            JSONParser parser = new JSONParser();
            JSONArray stateList = new JSONArray();
            try {
                reader = new FileReader("states.json");
                Object object = parser.parse(reader);
                stateList = (JSONArray) object;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JSONObject object = new JSONObject();
            object.put("productA", request.getProduct_A_amount());
            object.put("productB", request.getGetProduct_B_amount());
            object.put("date", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            stateList.add(object);
            try {
                file = new FileWriter("states.json");
                file.write(stateList.toJSONString());
                System.out.println("Logged " + object);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    file.flush();
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public VoteRequest requestVote( String ip, int port, VoteRequest voteRequest){
            VoteRequest voteResponse  = null;
            try{
                Socket socket = new Socket(ip,port);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                System.out.println("CONEXION CON "+port+" ESTABLECIDA PARA VOTE REQUEST");

                outputStream.writeObject(voteRequest);
                voteResponse = (VoteRequest) inputStream.readObject();
                System.out.println("Consumer Vote Request Response: "+voteResponse);

            }catch (Exception e){
                System.out.println("Ocurrio un error: "+e);
                e.printStackTrace();
            }
            return voteResponse;
        }

        public StateRegister getBackUp(String ip, int port) {
            StateRegister temporary = new StateRegister();
            try {
                Socket socket = new Socket("localhost", port);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("Connection succesful. Getting back up information...");

                outputStream.writeObject(temporary);
                temporary = (StateRegister) inputStream.readObject();
                System.out.println("Obtained response: " + temporary);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return temporary;
        }

        public void sendGlobalRequest( String ip, int port, GlobalRequest globalRequest){

            try{
                Socket socket = new Socket(ip,port);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                System.out.println("CONEXION CON "+port+" ESTABLECIDA PARA GLOBAL REQUEST");

                outputStream.writeObject(globalRequest);
               String response = (String) inputStream.readObject();
                System.out.println("Consumer Vote Request Response: " + response);

            }catch (Exception e){
                System.out.println("Ocurrio un error: "+e);
                e.printStackTrace();
            }

        }





        public void run() {

            try {
                System.out.println("Server accepted new client, Thread: "+Thread.currentThread().getId());
                in = new ObjectInputStream(clientSocket.getInputStream());
                out = new ObjectOutputStream(clientSocket.getOutputStream());


                Object request =  in.readObject(); //Receives a request
                System.out.println("REQUEST NAME: "+ request.getClass().getSimpleName());
                synchronized (this) { //A process is doing one activity at the time
                    switch (request.getClass().getSimpleName()) {
                        case "BackUpRequest": //The producers ask for a backup

                            BackUpRequest backUpRequest = (BackUpRequest) request;
                            System.out.println("BackUp request received: " + backUpRequest);
                            VoteRequest voteRequest = new VoteRequest();
                            //Establece conexion con el consumer

                            voteRequest = requestVote("localhost",4446, voteRequest); //CONSUMER

                            //Establece consexión con el producer
                            voteRequest = requestVote("localhost",4447, voteRequest); //PRODUCER
                            GlobalRequest globalRequest = null;
                            if(voteRequest.commit >=2){

                               saveStateJSON(backUpRequest);
                                StateRegister lastState = loadStateJSON();
                               globalRequest = new GlobalRequest(true, lastState); //Envia Global Commit con el State

                            }else{
                                globalRequest = new GlobalRequest(false);
                            }

                            sendGlobalRequest("localhost",4446, globalRequest); //ENVIA RESULTADO A CONSUMER
                            sendGlobalRequest("localhost",4447, globalRequest); //ENVIA RESULTADO A PRODUCER

                            out.writeObject(globalRequest); //PARA NOTIFICAR AL SERVER EL RESULTADO DE LA OPERACION

                            break;

                        case "StateRegister": // Restore a backup
                            StateRegister stateRegister = new StateRegister();
                            System.out.println("Request to restore received: " + stateRegister);
                            StateRegister consumerBackUp = getBackUp("localhost",4446); // Get BackUp from Consumer
                            StateRegister producerBackUp = getBackUp("localhost",4447); // Get BackUp from Producer
                            if (consumerBackUp.equals(producerBackUp)) out.writeObject(consumerBackUp);
                            else out.writeObject(stateRegister); // If different, send default request
                            break;
                        default:
                            System.out.println("Request not recognized, request received: " + request.getClass().getSimpleName());
                            //log("Server received an unrecognized Request : " + request);
                            break;
                    }

                    in.close();
                    out.close();
                    clientSocket.close();
                    System.out.println("Server closed socket for request:"+request+", Thread: "+Thread.currentThread().getId());
                    //log("Server closed socket for request:"+request+", Thread: "+Thread.currentThread().getId());
                }




            } catch (IOException
                    | ClassNotFoundException
                    e) {
                e.printStackTrace();
            }
        }
    }
}
