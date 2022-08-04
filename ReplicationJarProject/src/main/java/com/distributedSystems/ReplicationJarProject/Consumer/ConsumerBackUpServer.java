package com.distributedSystems.ReplicationJarProject.Consumer;

import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.BackUpCoordinator;
import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.VoteRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ConsumerBackUpServer {
//    private Socket socket;
    public ObjectOutputStream outputStream;
    public ObjectInputStream inputStream;
    private ServerSocket serverSocket = null;

    public static void main(String[] args){
        new ConsumerBackUpServer().startServer(4446);
    }

    public void startServer(int port) {

        try {
            System.out.println("SERVIDOR ESPERANDO CLIENTES EN PUERTO: "+port);
            serverSocket = new ServerSocket(port);

             Socket clientSocket =   serverSocket.accept();

             outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

             inputStream = new ObjectInputStream(clientSocket.getInputStream());

             System.out.println("CONEXION CON EL BACKUP COORDINATOR FUNCIONA");

                 Object request =  inputStream.readObject(); //Receives a request
                 switch (request.getClass().getSimpleName()) {
                     case "VoteRequest":
                         VoteRequest voteRequest = (VoteRequest) request;
                        //todo send back response
                         break;
                 }


        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: "+e);
            e.printStackTrace();
        }
    }


}
