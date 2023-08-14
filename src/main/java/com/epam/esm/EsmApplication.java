package com.epam.esm;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class EsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsmApplication.class, args);
    }
}
