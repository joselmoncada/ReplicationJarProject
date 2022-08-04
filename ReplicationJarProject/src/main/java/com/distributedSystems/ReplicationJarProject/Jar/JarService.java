package com.distributedSystems.ReplicationJarProject.Jar;

import com.distributedSystems.ReplicationJarProject.Responses.FillingResponse;
import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;
import org.springframework.stereotype.Service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
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
        System.out.println("RESULTADO JAR Service: "+response);
        return response;
    }

    public FillingResponse fillJar()  throws RemoteException {
        FillingResponse response = jarIf.fillJar();
        System.out.println("FILL JAR: "+ response);
        return response;
    }


    public String saveState()  throws RemoteException{
        return "En espera para confirmar cambios...";
    }

    public String restoreState()  throws RemoteException{
        return "Restaurando estado...";
    }
//
}

