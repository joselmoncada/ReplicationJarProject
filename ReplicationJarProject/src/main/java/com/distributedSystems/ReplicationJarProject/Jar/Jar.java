package com.distributedSystems.ReplicationJarProject.Jar;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Jar {
    private static final int port = 8099;
    public static void main(String[] args) throws RemoteException, AlreadyBoundException{
        Remote remote = UnicastRemoteObject.exportObject(new JarInterface() {

            @Override
            public void sendMovements() throws RemoteException {
                System.out.println("Yoohoo!");
            }

            @Override
            public void readTransactions() throws RemoteException {
                
            }
            
        }, 0);

        Registry registry = LocateRegistry.createRegistry(port);
        System.out.println("Listening on port " + String.valueOf(port));
        registry.bind("Jar", remote);
    }
}