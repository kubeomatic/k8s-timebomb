package io.kubeomatic.timebombadmission.service;

import io.kubernetes.client.openapi.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(fixedRate = 6000 )
    public void reportCurrentTime() throws IOException, ApiException {
        KubernetesClient.deleteExpiredPodsInCluster();
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
