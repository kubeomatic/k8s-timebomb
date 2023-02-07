package br.com.clusterlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@PropertySource("classpath:application-defaults.properties")
public class TimeBombAdmissionControllerApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(TimeBombAdmissionControllerApplication.class);
		application.setAdditionalProfiles("ssl");
		application.run(args);
	}

}
