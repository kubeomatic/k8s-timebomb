package br.com.clusterlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@PropertySource("classpath:application-defaults.properties")
public class K8sAdmissionControllerApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(K8sAdmissionControllerApplication.class);
		application.setAdditionalProfiles("ssl");
		application.run(args);
	}

}
