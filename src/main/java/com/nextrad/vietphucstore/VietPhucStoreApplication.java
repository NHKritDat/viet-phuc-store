package com.nextrad.vietphucstore;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VietPhucStoreApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().directory("./").load();
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
        System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
        System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
        System.setProperty("ACCESS_TOKEN_EXP", dotenv.get("ACCESS_TOKEN_EXP"));
        System.setProperty("REFRESH_TOKEN_EXP", dotenv.get("REFRESH_TOKEN_EXP"));
        System.setProperty("ISSUER", dotenv.get("ISSUER"));
        System.setProperty("AUDIENCE", dotenv.get("AUDIENCE"));

        SpringApplication.run(VietPhucStoreApplication.class, args);
    }

}
