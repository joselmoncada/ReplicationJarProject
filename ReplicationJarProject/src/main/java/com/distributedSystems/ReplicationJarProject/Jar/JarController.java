package com.distributedSystems.ReplicationJarProject.Jar;

import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ProductResponse getProduct(){
        return jarService.getProduct(12, "A");
    }

    @GetMapping("/get-movements")
    @ResponseBody
    public List<Register> getMovements(){
        return jarService.getMovements();
    }

    @GetMapping("/fill-jar")
    public List<Register> fillJar() {
        return jarService.fillJar();
    }

    @GetMapping("/save-state")
    public String saveState(){
       return jarService.saveState();
    }

    @GetMapping("/restore-state")
    public String restoreState(){
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
