package appsys.free.Finanzas;

/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;*/
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;*/

@SpringBootApplication

public class FinancesAppApplication  {
/*public class FinancesAppApplication implements CommandLineRunner {*/

    public static void main(String[] args) {
        SpringApplication.run(FinancesAppApplication.class, args);
    }

    /*   @Autowired
    private BCryptPasswordEncoder passwordEncoder;

 //Metodo adicional prueba para generar passwEncript
    @Override
    public void run(String... args) throws Exception {
        String passw = passwordEncoder.encode("cap.+45L**");
        System.out.println("pass: " + passw);

    }*/

}
