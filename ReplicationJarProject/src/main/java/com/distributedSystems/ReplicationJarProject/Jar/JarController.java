package com.distributedSystems.ReplicationJarProject.Jar;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/jar")
public class JarController {

    @GetMapping("/get-product")
    @ResponseBody
    public Product getProduct(){
        return new Product("A");
    }

    @GetMapping("/get-movements")
    @ResponseBody
    public List<Register> getMovements(){
        return
                List.of(
                        new Register("ADD PRODUCT"),
                        new Register("GET PRODUCT")
                );
    }

    @GetMapping("/fill-jar")
    public List<Register> fillJar() {
        return
                List.of(
                        new Register("ADD PRODUCT"),
                        new Register("ADD PRODUCT")
                );
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
