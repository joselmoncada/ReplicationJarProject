package com.distributedSystems.ReplicationJarProject.Jar;

import java.io.FileWriter;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.json.simple.JSONObject;

public class Jar {
    private static final int port = 8099;
    private static FileWriter file;
    public static void main(String[] args) throws RemoteException, AlreadyBoundException{
        Remote remote = UnicastRemoteObject.exportObject(new JarInterface() {

            @Override
            public List<Register> sendMovements() throws RemoteException {
                System.out.println("Reading movement JSON...");
                return null;
            }

            @Override
            public void readTransactions() throws RemoteException {
                
            }

            @Override
            public void logTransaction(Register register) throws RemoteException {
                JSONObject object = new JSONObject();
                object.put("name", register.getName());
                object.put("date", register.getDate().toString());
                try {
                    file = new FileWriter("movements.json");
                    file.write(object.toJSONString());
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
            
        }, 0);

        Registry registry = LocateRegistry.createRegistry(port);
        System.out.println("Listening on port " + String.valueOf(port));
        registry.bind("Jar", remote);
    }
}