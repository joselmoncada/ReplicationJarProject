package com.distributedSystems.ReplicationJarProject.Jar;

import com.distributedSystems.ReplicationJarProject.Responses.FillingResponse;
import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;

import java.io.FileReader;
import java.io.FileWriter;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JarRMIServer {
    private static final int port = 8099;
    private static FileWriter file;
    private static FileReader reader;

    public JarRMIServer() {

    }
    public static void main(String[] args) throws RemoteException, AlreadyBoundException{
        Jar jar = new Jar();
        Remote remote = UnicastRemoteObject.exportObject(new JarInterface() {
            // Returns list of transactions recorded in movements.json
            @Override
            public List<Register> sendMovements() throws RemoteException, NumberFormatException, ParseException {
                JSONParser parser = new JSONParser();
                JSONArray movementList = new JSONArray();
                List<Register> temporary = new ArrayList<Register>();
                System.out.println("Reading movement JSON...");
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
                for (Object object : movementList) {
                    JSONObject o = (JSONObject) object;
                    temporary.add(new Register(
                        o.get("name").toString(),
                        o.get("type").toString(),
                        Integer.valueOf(o.get("amount").toString()),
                        o.get("operation").toString(),
                        Integer.valueOf(o.get("remaining").toString()),
                        new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(o.get("date").toString())));
                }
                return temporary;
            }

            // Logs a transaction in movements.json
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
                object.put("type", register.getType());
                object.put("amount", register.getAmount());
                object.put("operation", register.getOperation());
                object.put("remaining", register.getRemaining());
                object.put("date", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(register.getDate()));
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

            @Override
            public ProductResponse getProduct (int number, String type) throws RemoteException{
                System.out.println("RESULTADO JAR RMI");
                 return jar.getProduct(number, type);
            }

            @Override
            public FillingResponse fillJar() throws RemoteException {
                return jar.fillJar();
            }

            @Override
            public int getProducts_B() throws RemoteException {
                return jar.getProducts_A();
            }

            @Override
            public int getProducts_A() throws RemoteException {
                return jar.getProducts_B();
            }


        }, 0);

        Registry registry = LocateRegistry.createRegistry(port);
        System.out.println("Listening on port " + String.valueOf(port));
        registry.bind("Jar", remote);
    }


    private static class Jar {
        int products_A;
        int products_B;

        public Jar() {
            this.products_A = 60;
            this.products_B = 40;
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
            System.out.println("RESULTADO JAR: "+response);
            return response;
        }

        public FillingResponse fillJar() {
            FillingResponse response = new FillingResponse();
            response.setPrevious_A(products_A);
            response.setPrevious_B(products_B);
            products_A = products_A + 60;
            products_B = products_B + 40;

            response.setCurrent_A(products_A);
            response.setCurrent_B(products_B);
            System.out.println("FillJar requested: \n" +
                    "A products: " + products_A + ", \n" +
                    "B products: " + products_B);
            return response;
        }

        public String saveState() {
            return "En espera para confirmar cambios...";
        }

        public String restoreState() {
            return "Restaurando estado...";
        }

    }
}
