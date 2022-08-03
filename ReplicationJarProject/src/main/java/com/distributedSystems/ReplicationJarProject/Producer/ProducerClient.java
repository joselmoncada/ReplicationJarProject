package com.distributedSystems.ReplicationJarProject.Producer;

import com.distributedSystems.ReplicationJarProject.Consumer.ConsumerService;

import java.util.Scanner;

public class ProducerClient {


    public static void main(String[] args) {
        ProducerService service = new ProducerService();
        Scanner sc = new Scanner(System.in);
        int option = -1;
        do {
        System.out.println("Operaciones \n" +
                "1. Fill Jar \n" +
                "2. Save State \n" +
                "3. Restore Last State");

        option = sc.nextInt();

        switch (option) {

            case 1:
                service.fillJar();
                break;
            case 2:
                service.saveState();
                break;
            case 3:
                service.restoreLastState();
                break;

        }

    }while (true);

}

}
