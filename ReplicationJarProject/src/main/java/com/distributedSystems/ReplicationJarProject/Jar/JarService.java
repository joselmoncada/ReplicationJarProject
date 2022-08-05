package com.distributedSystems.ReplicationJarProject.Jar;

import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.RestoreRequest;
import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.GlobalRequest;
import com.distributedSystems.ReplicationJarProject.BackUpCoordinator.VoteRequest;
import com.distributedSystems.ReplicationJarProject.Producer.BackUpRequest;
import com.distributedSystems.ReplicationJarProject.Responses.FillingResponse;
import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class JarService {
    private static final String IP = "127.0.0.1";
    private static final int PUERTO = 1100;
    private Registry registry;
    private JarInterface jarIf;

    public JarService() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry("localhost", 8099);
        jarIf = (JarInterface) registry.lookup("Jar");

    }

    public List<Register> getMovements() throws RemoteException, NumberFormatException, ParseException{
        return jarIf.sendMovements();
    }

    public ProductResponse getProduct(int number, String type) throws RemoteException {
        ProductResponse response = jarIf.getProduct(number, type);
        System.out.println("GET PRODUCT JAR Service: "+response);
        jarIf.logTransaction(new Register("Consumer", type, number, "GET"));
        return response;
    }

    public FillingResponse fillJar()  throws RemoteException {
        FillingResponse response = jarIf.fillJar();
        System.out.println("FILL JAR: "+ response);
        jarIf.logTransaction(new Register("Producer", "A", 60, "FILL"));
        jarIf.logTransaction(new Register("Producer", "B", 40, "FILL"));
        return response;
    }


    public String saveState()  throws RemoteException{


        try{
            Socket socket = new Socket("localhost",4444);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            System.out.println("CONEXION CON EL BACKUP COORDINATOR ESTABLECIDA");
            BackUpRequest backUpRequest =  new BackUpRequest(
                    jarIf.getProducts_A(),
                    jarIf.getProducts_B(),
                    new Date()
            );
            outputStream.writeObject(backUpRequest);
            GlobalRequest response = (GlobalRequest) inputStream.readObject();
            System.out.println("MAIN SERVER COMMIT RESULT: "+ response);


        }catch (Exception e){
            System.out.println("Ocurrio un error: "+e);
            e.printStackTrace();
        }

        JSONObject response = new JSONObject();
        response.put("message", "Waiting...");

        return response.toJSONString();
    }

    public String restoreState()  throws RemoteException{

        try {
            Socket socket = new Socket("localhost",4444);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to the backup coordinator.");

            RestoreRequest request = new RestoreRequest();
            outputStream.writeObject(request);
            RestoreRequest response = (RestoreRequest) inputStream.readObject();
            if (response.requestValid())
                jarIf.loadProducts(response.getProductA(), response.getProductB());
            else
                System.out.println("Failed to restore backup: One of the backups received was different.");
        } catch (Exception e) {
            //TODO: handle exception
        }

        JSONObject response = new JSONObject();
        response.put("message", "Waiting...");

        return response.toJSONString();
    }
//
}

