package io.kubeomatic.timebombscheduler.service;

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


    @Scheduled(cron = "${cron.expression}")
    public void reportCurrentTime() throws IOException, ApiException {
        KubernetesClient.deleteExpiredPodsInCluster();
        log.info("Nothing more to do. Waiting next run to identify expired PODs to delete. Server time is: EPOCH=" + Epoch.dateToEpoch().toString() + ", Human=\"" + Epoch.epochToDate(Epoch.dateToEpoch()) + "\"");
    }
}
