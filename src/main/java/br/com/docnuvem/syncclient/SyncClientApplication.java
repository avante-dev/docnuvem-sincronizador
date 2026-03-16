package br.com.docnuvem.syncclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SyncClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(SyncClientApplication.class, args);
    }
}
