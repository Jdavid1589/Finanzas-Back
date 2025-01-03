package appsys.free.constru_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication

public class ConstruAppApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ConstruAppApplication.class, args);
	}

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	//Metodo adicional prueba para generar passwEncript
	@Override
	public void run(String... args) throws Exception {
		String passw=passwordEncoder.encode("cap.+45L**");
		System.out.println("pass: "+passw);

	}

}
