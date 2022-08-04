package com.distributedSystems.ReplicationJarProject.Consumer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConsumerBackUpServer {
    private Socket socket;
    public ObjectOutputStream outputStream;
    public ObjectInputStream inputStream;


    public static void main(String[] args){
        new ConsumerBackUpServer().startConnectionWithBackUpCoordinator("localhost",4444);



    }

    public void startConnectionWithBackUpCoordinator(String ip, int port) {
         try{
             socket = new Socket(ip, port);
             outputStream = new ObjectOutputStream(socket.getOutputStream());

             inputStream = new ObjectInputStream(socket.getInputStream());

             System.out.println("CONEXION CON EL BACKUP COORDINATOR FUNCIONA");
             while(true){




            }


        }catch (Exception e){
            System.out.println("Ocurrio un error: "+e);
            e.printStackTrace();
        }
    }


}
