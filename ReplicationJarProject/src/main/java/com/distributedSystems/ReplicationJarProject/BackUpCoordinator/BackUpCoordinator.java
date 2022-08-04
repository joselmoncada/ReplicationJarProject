package com.distributedSystems.ReplicationJarProject.BackUpCoordinator;

import com.distributedSystems.ReplicationJarProject.Jar.Register;
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

        public void logTransaction(BackUpRequest request) throws RemoteException {
            JSONParser parser = new JSONParser();
            JSONArray movementList = new JSONArray();
            try {
                reader = new FileReader("states.json");
                Object object = parser.parse(reader);
                movementList = (JSONArray) object;
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
            movementList.add(object);
            try {
                file = new FileWriter("movements.json");
                file.write(movementList.toJSONString());
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
                            try{
                                Socket socket = new Socket("localhost",4446);
                                ObjectOutputStream consumerOutputStream = new ObjectOutputStream(socket.getOutputStream());

                                ObjectInputStream consumerInputStream = new ObjectInputStream(socket.getInputStream());

                                System.out.println("CONEXION CON EL CONSUMER ESTABLECIDA");

                                consumerOutputStream.writeObject(voteRequest);
                                VoteRequest voteConsumerResponse = (VoteRequest) consumerInputStream.readObject();
                                System.out.println("Consumer Vote Request Response: "+voteConsumerResponse);
                                voteRequest.setAbort(voteConsumerResponse.getAbort());
                                voteRequest.setCommit(voteConsumerResponse.getCommit());
                            }catch (Exception e){
                                System.out.println("Ocurrio un error: "+e);
                                e.printStackTrace();
                            }
                            //Establece consexión con el producer

                            try{
                                Socket socket = new Socket("localhost",4447);
                                ObjectOutputStream producerOutputStream = new ObjectOutputStream(socket.getOutputStream());

                                ObjectInputStream producerInputStream = new ObjectInputStream(socket.getInputStream());

                                System.out.println("CONEXION CON EL Producer ESTABLECIDA");

                                producerOutputStream.writeObject(voteRequest);
                                VoteRequest voteProducerResponse = (VoteRequest) producerInputStream.readObject();
                                System.out.println("Producer Vote Request Response: "+voteProducerResponse);
                                voteRequest.setAbort(voteProducerResponse.getAbort());
                                voteRequest.setCommit(voteProducerResponse.getCommit());
                            }catch (Exception e){
                                System.out.println("Ocurrio un error: "+e);
                                e.printStackTrace();
                            }
                            System.out.println("BackUp request fulfilled: "+voteRequest);

                            break;
                        case "VoteRequest": //Receives the vote response of producer or consumer

//                            VoteRequest voteRequest = (VoteRequest) request;
//                            System.out.println("Vote Request received: " + voteRequest);

                           //todo manejar logica de votos para el global commit

                            //log("Ingredient Request response: " + response);

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
