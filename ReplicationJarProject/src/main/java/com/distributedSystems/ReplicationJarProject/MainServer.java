package com.distributedSystems.ReplicationJarProject;

import com.distributedSystems.ReplicationJarProject.Jar.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;


@SpringBootApplication(scanBasePackages = { "com.distributedSystems.ReplicationJarProject" })
@RestController
public class MainServer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
	private static final String IP = "127.0.0.1";
	private static final int JAR_PORT = 1100;

	public static void main(String[] args)  throws RemoteException, NotBoundException {
		SpringApplication.run(MainServer.class, args);
		Registry registry = LocateRegistry.getRegistry(IP, JAR_PORT);
//		JarInterface server = (JarInterface) registry.lookup("Jar");


	}

	@Override
	public void customize(ConfigurableWebServerFactory factory) {
		factory.setPort(8100);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

}
