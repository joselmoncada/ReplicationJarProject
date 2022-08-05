package com.distributedSystems.ReplicationJarProject.Jar;


import com.distributedSystems.ReplicationJarProject.Responses.FillingResponse;
import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(path = "api/v1/jar")
public class JarController {

    private final JarService jarService;

    @Autowired
    public JarController(JarService service) {
        this.jarService = service;
    }

    @GetMapping("/get-product")
    @ResponseBody
    public ProductResponse getProduct() throws RemoteException{
        int amount = ThreadLocalRandom.current().nextInt(1, 20 + 1);
        boolean bool_type = new Random().nextBoolean();
        String type;
        if(bool_type){
            type = "A";
        }else{
            type="B";
        }
        ProductResponse response = jarService.getProduct(amount, type);
        System.out.println("RESULTADO JAR CONTROLLER: "+response);
        return response;

    }

    @GetMapping("/get-movements")
    @ResponseBody
    public List<Register> getMovements() throws RemoteException, NumberFormatException, ParseException{
        List<Register> movements = jarService.getMovements();
        System.out.println("MOVEMENTS: "+ movements);
        return movements;
    }

    @GetMapping("/fill-jar")
    public FillingResponse fillJar()  throws RemoteException{

        return jarService.fillJar();

    }

    @GetMapping("/save-state")
    public String saveState()  throws RemoteException {
        System.out.println("SAVE STATE ENDPOINT ");
       return jarService.saveState();
    }

    @GetMapping("/restore-state")
    public String restoreState()  throws RemoteException{
        System.out.println("RESTORE STATE ENDPOINT ");
        return jarService.restoreState();
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
