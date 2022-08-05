package com.distributedSystems.ReplicationJarProject.Consumer;

import com.distributedSystems.ReplicationJarProject.Jar.Register;
import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ConsumerService {


    final String  GET_PRODUCT_URL = "http://172.27.185.163:8100/api/v1/jar/get-product";
    final String  GET_MOVEMENTS_URL = "http://172.27.185.163:8100/api/v1/jar/get-movements";


    RestTemplate restTemplate;

    public ConsumerService() {
        restTemplate  = new RestTemplate();
    }

    public ProductResponse getProduct(){
        ProductResponse response = restTemplate.getForObject(GET_PRODUCT_URL, ProductResponse.class);
        System.out.println("GET PRODUCT RESPONSE: "+response);
        return response;
    }

    public List<Register> getMovements(){
        Register[] response = restTemplate.getForObject(GET_MOVEMENTS_URL, Register[].class);
        System.out.println("GET MOVEMENTS RESPONSE: "+response);
        return Arrays.asList(response);
    }
}
