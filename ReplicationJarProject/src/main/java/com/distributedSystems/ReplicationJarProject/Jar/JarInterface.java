package com.distributedSystems.ReplicationJarProject.Jar;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.List;

public interface JarInterface extends Remote{

    public List<Register> sendMovements() throws RemoteException, NumberFormatException, ParseException;
    public void logTransaction(Register register) throws RemoteException;

}
