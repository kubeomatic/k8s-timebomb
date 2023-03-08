package io.kubeomatic.timebombadmission;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.PrintStream;

//@EnableScheduling
@SpringBootApplication
public class TimebombAdmissionApplication {

    public static void main(String[] args) {
//        SpringApplication.run(TimebombAdmissionApplication.class, args);
        SpringApplication application = new SpringApplication(TimebombAdmissionApplication.class);
        application.setAdditionalProfiles("ssl");
        application.run(args);
    }

}
