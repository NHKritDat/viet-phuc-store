package com.nextrad.vietphucstore;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Objects;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class VietPhucStoreApplication {

    public static void main(String[] args) {
        readEnv();

        SpringApplication.run(VietPhucStoreApplication.class, args);
    }

    private static void readEnv() {
        Dotenv dotenv = Dotenv.configure().directory("./").load();
        System.setProperty("DB_URL", Objects.requireNonNull(dotenv.get("DB_URL")));
        System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
        System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
        System.setProperty("MAIL_USERNAME", Objects.requireNonNull(dotenv.get("MAIL_USERNAME")));
        System.setProperty("MAIL_PASSWORD", Objects.requireNonNull(dotenv.get("MAIL_PASSWORD")));
        System.setProperty("JWT_SECRET_KEY", Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY")));
        System.setProperty("ACCESS_TOKEN_EXP", Objects.requireNonNull(dotenv.get("ACCESS_TOKEN_EXP")));
        System.setProperty("REFRESH_TOKEN_EXP", Objects.requireNonNull(dotenv.get("REFRESH_TOKEN_EXP")));
        System.setProperty("ISSUER", Objects.requireNonNull(dotenv.get("ISSUER")));
        System.setProperty("AUDIENCE", Objects.requireNonNull(dotenv.get("AUDIENCE")));
        System.setProperty("VERIFY_EMAIL_SUBJECT", Objects.requireNonNull(dotenv.get("VERIFY_EMAIL_SUBJECT")));
        System.setProperty("VERIFY_EMAIL_CONTENT", Objects.requireNonNull(dotenv.get("VERIFY_EMAIL_CONTENT")));
        System.setProperty("VERIFY_EMAIL_URL", Objects.requireNonNull(dotenv.get("VERIFY_EMAIL_URL")));
        System.setProperty("RESET_PASSWORD_SUBJECT", Objects.requireNonNull(dotenv.get("RESET_PASSWORD_SUBJECT")));
        System.setProperty("RESET_PASSWORD_CONTENT", Objects.requireNonNull(dotenv.get("RESET_PASSWORD_CONTENT")));
        System.setProperty("RESET_PASSWORD_URL", Objects.requireNonNull(dotenv.get("RESET_PASSWORD_URL")));
    }

}
