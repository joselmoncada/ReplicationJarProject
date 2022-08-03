package com.distributedSystems.ReplicationJarProject.Jar;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface JarInterface extends Remote{

    public List<Register> sendMovements() throws RemoteException;
    public void readTransactions() throws RemoteException;
    public void logTransaction(Register register) throws RemoteException;

}
