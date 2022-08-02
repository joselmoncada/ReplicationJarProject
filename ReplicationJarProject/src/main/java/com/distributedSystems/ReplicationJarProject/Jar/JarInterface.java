package com.distributedSystems.ReplicationJarProject.Jar;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JarInterface extends Remote{
    public void sendMovements() throws RemoteException;
    public void readTransactions() throws RemoteException;
}
