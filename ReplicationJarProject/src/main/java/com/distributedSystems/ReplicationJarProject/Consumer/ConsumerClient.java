package com.distributedSystems.ReplicationJarProject.Consumer;

import com.distributedSystems.ReplicationJarProject.Jar.Register;
import com.distributedSystems.ReplicationJarProject.Responses.ProductResponse;
import org.assertj.core.api.Assertions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class ConsumerClient {



    public static void main(String[] args) {
        ConsumerService service = new ConsumerService();
        Scanner sc = new Scanner(System.in);
        int option = -1;
        do {
            System.out.println("Operaciones \n" +
                    "1. Get Product \n" +
                    "2. Get Movements \n");

            option  = sc.nextInt();

            switch (option){

                case 1:
                    service.getProduct();
                    break;
                case 2:
                    service.getMovements();
                    break;

            }


        }while (true);

    }






}
