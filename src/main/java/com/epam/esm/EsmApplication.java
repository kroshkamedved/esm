package com.epam.esm;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableEncryptableProperties
public class EsmApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.setProperty("jasypt.encryptor.password", "key");
        SpringApplication.run(EsmApplication.class, args);
    }
}
