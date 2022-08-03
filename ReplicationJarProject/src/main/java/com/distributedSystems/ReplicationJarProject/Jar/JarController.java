package com.distributedSystems.ReplicationJarProject.Jar;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/jar")
public class JarController {

    private Registry registry;
    private JarInterface jarIf;

    public JarController() throws RemoteException, NotBoundException{
        registry = LocateRegistry.getRegistry("localhost", 8099);
        jarIf = (JarInterface) registry.lookup("Jar");
    }

    @GetMapping("/get-product")
    @ResponseBody
    public Product getProduct() throws RemoteException{
        jarIf.logTransaction(new Register("Actor", 'A', 1, "GET", 0));
        return new Product("A");
    }

    @GetMapping("/get-movements")
    @ResponseBody
    public List<Register> getMovements(){
        return null;
                /*List.of(
                        new Register("ADD PRODUCT"),
                        new Register("GET PRODUCT")
                );*/
    }

    @GetMapping("/fill-jar")
    public List<Register> fillJar() {
        return null;
                /*List.of(
                        new Register("ADD PRODUCT"),
                        new Register("ADD PRODUCT")
                );*/
    }

    @GetMapping("/save-state")
    public String saveState(){
        return "En espera para confirmar cambios...";
    }

    @GetMapping("/restore-state")
    public String restoreState(){
        return "Restaurando estado...";
    }
//
//    (POST) fillJar
//
//(POST) saveState
//            (POST) restoreLastState

//    @GetMapping("/productRequest")
//    public String getProduct(){
//        JSONObject product = new JSONObject();
//        try {
//
//            product.put("Type","A");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return product.toString();





}
