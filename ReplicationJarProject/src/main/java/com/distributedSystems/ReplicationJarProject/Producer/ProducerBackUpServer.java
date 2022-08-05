package com.distributedSystems.ReplicationJarProject.Producer;

import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.GlobalRequest;
import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.RestoreRequest;
import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.VoteRequest;
import com.distributedSystems.ReplicationJarProject.Consumer.ConsumerBackUpServer;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ProducerBackUpServer {

    private Socket socket;
    public ObjectOutputStream outputStream;
    public ObjectInputStream inputStream;
    private ServerSocket serverSocket = null;
    private FileReader reader;

    public static void main(String[] args){
        new ProducerBackUpServer().startServer(4447);

    }

    public RestoreRequest loadStateJSON() {
        JSONParser parser = new JSONParser();
        JSONArray stateList = new JSONArray();
        RestoreRequest temporary = new RestoreRequest();
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
    
    public void startServer(int port) {

        try {
            System.out.println("SERVIDOR ESPERANDO CLIENTES EN PUERTO: "+port);
            serverSocket = new ServerSocket(port);

            while (true) {
            Socket clientSocket =   serverSocket.accept();

            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            System.out.println("CONEXION CON EL BACKUP COORDINATOR ");

            Object request =  inputStream.readObject(); //Receives a request
            switch (request.getClass().getSimpleName()) {
                case "VoteRequest":
                    Random rand = new Random();
                    VoteRequest voteRequest = (VoteRequest) request;
                    if (rand.nextBoolean()) voteRequest.setCommit(voteRequest.getCommit() + 1);
                    else voteRequest.setAbort(voteRequest.getAbort() + 1);
                    outputStream.writeObject(voteRequest);
                    break;
                case "RestoreRequest":
                    System.out.println("Sending local backup");
                    outputStream.writeObject(loadStateJSON());
                    break; 
                case "GlobalRequest":
                    GlobalRequest globalRequest = (GlobalRequest) request;
                    if(globalRequest.isCommit()){
                        System.out.println("GLOBAL_COMMIT: SE HAN CONFIRMADO LOS CAMBIOS");
                        outputStream.writeObject(new String("PRODUCER: COMMIT CONFIRMADO"));
                    }else{
                        System.out.println("GLOBAL_ABORT: COMMIT ABORTADO");
                        outputStream.writeObject(new String("PRODUCER: COMMIT ABORTADO"));
                    }


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
