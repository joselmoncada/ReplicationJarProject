package com.distributedSystems.ReplicationJarProject.Consumer;

import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.GlobalRequest;
import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.StateRegister;
import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.VoteRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.distributedSystems.ReplicationJarProject.Producer.BackUpRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConsumerBackUpServer {
//    private Socket socket;
    public ObjectOutputStream outputStream;
    public ObjectInputStream inputStream;
    private ServerSocket serverSocket = null;
    private FileReader reader;
    private  FileWriter file;

    public static void main(String[] args){
        new ConsumerBackUpServer().startServer(4446);
    }

    public void saveStateJSON(StateRegister request) throws RemoteException {
        JSONParser parser = new JSONParser();
        JSONArray stateList = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("productA", request.getProductA());
        object.put("productB", request.getProductB());
        object.put("date", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        stateList.add(object);
        try {
            file = new FileWriter("consumerStates.json");
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

    public StateRegister loadStateJSON() {
        JSONParser parser = new JSONParser();
        JSONArray stateList = new JSONArray();
        StateRegister temporary = new StateRegister();
        System.out.println("Loading states...");
    
        // Reading the JSON and getting the States
        try {
            reader = new FileReader("consumerStates.json");
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
    
    public void startServer(int port) {

        try {
            System.out.println("SERVIDOR ESPERANDO CLIENTES EN PUERTO: "+port);
            serverSocket = new ServerSocket(port);

            while (true) {
             Socket clientSocket =   serverSocket.accept();

             outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

             inputStream = new ObjectInputStream(clientSocket.getInputStream());

             System.out.println("CONEXION CON EL BACKUP COORDINATOR FUNCIONA");

                 Object request =  inputStream.readObject(); //Receives a request
                 switch (request.getClass().getSimpleName()) {
                     case "VoteRequest":
                        Random rand = new Random();
                        VoteRequest voteRequest = (VoteRequest) request;
                        if (rand.nextBoolean()) voteRequest.setCommit(voteRequest.getCommit() + 1);
                        else voteRequest.setAbort(voteRequest.getAbort() + 1);
                        outputStream.writeObject(voteRequest);
                        break;
                     case "GlobalRequest":
                         GlobalRequest globalRequest = (GlobalRequest) request;
                         if(globalRequest.isCommit()){
                             System.out.println("GLOBAL_COMMIT: SE HAN CONFIRMADO LOS CAMBIOS");
                             saveStateJSON(globalRequest.getStateRegister());
                             outputStream.writeObject(new String("CONSUMER: COMMIT CONFIRMADO"));
                         }else{
                             System.out.println("GLOBAL_ABORT: COMMIT ABORTADO");
                             outputStream.writeObject(new String("CONSUMER: COMMIT ABORTADO"));
                         }

                         break;
                    case "RestoreRequest":
                        System.out.println("Sending local backup");
                        outputStream.writeObject(loadStateJSON());
                        break; 
                     default:
                         break;
                 }
                inputStream.close();
                outputStream.close();
                clientSocket.close();
                System.out.println("Server closed socket for request:"+request+", Thread: "+Thread.currentThread().getId());
            }


        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: "+e);
            e.printStackTrace();
        }
    }


}
