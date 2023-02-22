package io.kubeomatic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@PropertySource("classpath:application-defaults.properties")
public class TimeBombAdmissionControllerApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(TimeBombAdmissionControllerApplication.class);
		application.setAdditionalProfiles("ssl");
		application.run(args);
	}

}
