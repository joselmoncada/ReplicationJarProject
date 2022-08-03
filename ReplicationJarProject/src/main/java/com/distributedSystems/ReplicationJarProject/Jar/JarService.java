package com.distributedSystems.ReplicationJarProject.Jar;

import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class JarService {
    private static final String IP = "127.0.0.1";
    private static final int PUERTO = 1100;
    private final Jar jar;

    public JarService() {
        this.jar = new Jar();
    }

    public List<Register> getMovements(){
        return
                List.of(
                        new Register("ADD PRODUCT"),
                        new Register("GET PRODUCT")
                );
    }

    public ProductResponse getProduct(int number, String type) {
        return  jar.getProduct(number, type);

    }

    public List<Register> fillJar() {
        return
                List.of(
                        new Register("ADD PRODUCT"),
                        new Register("ADD PRODUCT")
                );
    }


    public String saveState(){
        return "En espera para confirmar cambios...";
    }

    public String restoreState(){
        return "Restaurando estado...";
    }
//
}

