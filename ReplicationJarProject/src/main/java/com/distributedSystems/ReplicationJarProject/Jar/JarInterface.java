package com.distributedSystems.ReplicationJarProject.Jar;

import com.distributedSystems.ReplicationJarProject.Responses.FillingResponse;
import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.List;

public interface JarInterface extends Remote{

    public List<Register> sendMovements() throws RemoteException, NumberFormatException, ParseException;
    public void logTransaction(Register register) throws RemoteException;
    public ProductResponse getProduct(int number, String type)  throws RemoteException;
    public FillingResponse fillJar()  throws RemoteException;
    public int getProducts_B() throws RemoteException;
    public int getProducts_A() throws RemoteException;
    public void loadProducts(int prodA, int prodB) throws RemoteException;
}
