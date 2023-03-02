package io.kubeomatic.timebombscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TimebombSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimebombSchedulerApplication.class, args);
    }

}
