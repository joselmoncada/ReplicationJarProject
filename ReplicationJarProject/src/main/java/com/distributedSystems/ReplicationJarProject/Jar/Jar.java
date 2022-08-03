package com.distributedSystems.ReplicationJarProject.Jar;

import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;

import java.io.FileReader;
import java.io.FileWriter;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Jar {
    private static final int port = 8099;
    private static FileWriter file;
    private static FileReader reader;
    int products_A;
    int products_B;

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
                JSONParser parser = new JSONParser();
                JSONArray movementList = new JSONArray();
                try {
                    reader = new FileReader("movements.json");
                    Object object = parser.parse(reader);
                    movementList = (JSONArray) object;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                JSONObject object = new JSONObject();
                object.put("name", register.getName());
                object.put("date", register.getDate().toString());
                movementList.add(object);
                try {
                    file = new FileWriter("movements.json");
                    file.write(movementList.toJSONString());
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

    public int getProducts_A() {
        return products_A;
    }

    public void setProducts_A(int products_A) {
        this.products_A = products_A;
    }

    public int getProducts_B() {
        return products_B;
    }

    public void setProducts_B(int products_B) {
        this.products_B = products_B;
    }

    public Jar() {
        this.products_A = 60;
        this.products_B = 40;
    }


    public ProductResponse getProduct(int number, String type) {
        ProductResponse response = null;
        if (type == "A") {
            if (products_A >= number) {
                products_A = products_A - number;
                response = new ProductResponse(number, type);
            }

        } else {
            if (products_B >= number) {
                products_B = products_B - number;
                response = new ProductResponse(number, type);
            }
        }

        return response;
    }

    public List<Register> fillJar() {
        products_A = products_A + 60;
        products_B = products_B + 40;
        System.out.println("FillJar requested: \n" +
                "A products: " + products_A + ", \n" +
                "B products: " + products_B);
        return
                List.of(
                        new Register("ADD 60 PRODUCT A"),
                        new Register("ADD 40 PRODUCT B")
                );
    }


    public String saveState() {
        return "En espera para confirmar cambios...";
    }

    public String restoreState() {
        return "Restaurando estado...";
    }
}
