package io.kubeomatic.timebombadmission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

    //@EnableScheduling
@SpringBootApplication
public class TimebombAdmissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimebombAdmissionApplication.class, args);
    }

}
