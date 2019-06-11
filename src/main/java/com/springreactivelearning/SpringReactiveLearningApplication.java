package com.springreactivelearning;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.validation.Valid;

@SpringBootApplication
public class SpringReactiveLearningApplication {

    @Value("${data.mongodb.database}")
    static  String db;
	public static void main(String[] args) {
	    System.out.println("DB" + db);
		SpringApplication.run(SpringReactiveLearningApplication.class, args);
	}

}
