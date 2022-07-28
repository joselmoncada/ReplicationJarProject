package com.distributedSystems.ReplicationJarProject.Jar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/jar")
public class JarController {

    @GetMapping
    public Product getProduct(){
        return new Product("A");
    }
}
