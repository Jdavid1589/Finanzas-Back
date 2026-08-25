package appsys.free.Agrocore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
// public class ProyectoBaseApplication implements CommandLineRunner { //para la implement
public class AppAgroCoreBackApplication {
//public class FinancesAppApplication  {

    public static void main(String[] args) {
        SpringApplication.run(AppAgroCoreBackApplication.class, args);

        // Genera el hash y muéstralo antes de arrancar Spring
       // System.out.println("HASH: " + new BCryptPasswordEncoder().encode("admin123"));
        System.out.println("HASH: " + new BCryptPasswordEncoder().encode("1234"));
    }




}
