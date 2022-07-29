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

import java.util.List;


@SpringBootApplication(scanBasePackages = { "com.distributedSystems.ReplicationJarProject" })
@RestController
public class MainServer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

	public static void main(String[] args) {
		SpringApplication.run(MainServer.class, args);
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
