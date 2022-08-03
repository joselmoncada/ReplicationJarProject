package com.distributedSystems.ReplicationJarProject.Jar;

import com.distributedSystems.ReplicationJarProject.Responses.FillingResponse;
import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

@Service
public class JarService {
    private static final String IP = "127.0.0.1";
    private static final int PUERTO = 1100;
    private final Jar jar;
    private Registry registry;
    private JarInterface jarIf;

    public JarService() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry("localhost", 8099);
        jarIf = (JarInterface) registry.lookup("Jar");
        jar = new Jar();
    }

    public List<Register> getMovements(){

        return
                List.of(
                        new Register("ADD PRODUCT"),
                        new Register("GET PRODUCT")
                );
    }

    public ProductResponse getProduct(int number, String type) {
        //jarIf.logTransaction(new Register("Actor", "A", 1, "GET", 0));
        return  jar.getProduct(number, type);
    }

    public FillingResponse fillJar() {
        FillingResponse response = new FillingResponse(60,40,120, 80);
        System.out.println("FILL JAR: "+ response);
        return response;

    }


    public String saveState(){
        return "En espera para confirmar cambios...";
    }

    public String restoreState(){
        return "Restaurando estado...";
    }
//
}

