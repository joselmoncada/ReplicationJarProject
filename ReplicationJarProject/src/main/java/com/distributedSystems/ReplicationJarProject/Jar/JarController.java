package com.distributedSystems.ReplicationJarProject.Jar;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/jar")
public class JarController {

    @GetMapping("/product")
    public String getProductString(){
        return "PRODUCTO";
    }

    @GetMapping("/product2")
    @ResponseBody
    public Product getProduct2(){
        return new Product("A");
    }

    @GetMapping("/productRequest")
    public String getProduct(){
        JSONObject product = new JSONObject();
        try {

            product.put("Type","A");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return product.toString();


    }


}
