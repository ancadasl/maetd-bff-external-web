package es.maetd.marketcreators.bff.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = "es.maetd.marketcreators",
        exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class BffExternalWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffExternalWebApplication.class, args);
    }
}
