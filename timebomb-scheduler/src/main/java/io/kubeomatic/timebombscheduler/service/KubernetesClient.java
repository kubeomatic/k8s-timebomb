package io.kubeomatic.timebombscheduler.service;
import io.kubeomatic.timebombscheduler.config.AppProperties;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1DeleteOptions;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.lang.Long.parseLong;

public class KubernetesClient {
    public static Logger logger = LoggerFactory.getLogger(KubernetesClient.class);
    public static void deleteExpiredPodsInCluster() throws IOException, ApiException {
        try {

            String[] values = AppProperties.getProperty(AppProperties.labelSelectorValue).split(";");
            for ( String value : values) {
                logger.info("Searching pods to delete. MatchLabel=" + AppProperties.getProperty(AppProperties.labelSelectorKey) + "=" + value);
                ApiClient client = ClientBuilder.cluster().build();
                Configuration.setDefaultApiClient(client);
                CoreV1Api api = new CoreV1Api();

                V1PodList list = api.listPodForAllNamespaces(
                        null,
                        null,
                        null,
                        AppProperties.getProperty(AppProperties.labelSelectorKey) + "=" + value,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

                for (V1Pod item : list.getItems())
                {

                    String namespacePod = item.getMetadata().getNamespace() + "/" + item.getMetadata().getName();
                    logger.info("Checking validity of " + namespacePod);
                    boolean matchValidityAnnotation = false;
                    boolean matchLabelEnabled = false;
                    Integer labelCount = 0;
                    for (Map.Entry<String,String> label: item.getMetadata().getLabels().entrySet())
                    {
                        labelCount++;
                        Integer numberOfLabels = item.getMetadata().getLabels().size();
                        if ( matchLabelEnabled )
                        {
                            continue;
                        }
                        boolean labelTimebomKey = label.getKey().equals(AppProperties.getProperty(AppProperties.propertyLabelTimebomb));
                        boolean labelTimebomValue = label.getValue().toLowerCase().equals("enabled");
                        if ( (labelTimebomKey) && (labelTimebomValue ) )
                        {
                            matchLabelEnabled = true;
                            logger.info("Pod " + namespacePod + " have timebomb label enabled.");
                            Integer numberOfAnnoations = item.getMetadata().getAnnotations().entrySet().size();
                            Integer annotationCount = 0;
                            for (Map.Entry<String,String> annotation: item.getMetadata().getAnnotations().entrySet())
                            {
                                annotationCount++;
                                if ( matchValidityAnnotation)
                                {
                                    continue;
                                }

                                boolean annotationValidityKey = annotation.getKey().equals(AppProperties.getProperty(AppProperties.propertyAnnotationValidity));
                                if (annotationValidityKey){
//                                Long annotationValidityValue = Long.valueOf(annotation.getValue());
                                    Long annotationValidityValue = parseLong(annotation.getValue());
                                    logger.debug("DATE EPOCH Pod=" + namespacePod + " STRING=" + annotation.getValue() + ". Converted=" + annotationValidityValue + ". H_L=\"" + Epoch.epochToDate(annotationValidityValue) + "\"" );
                                    if ( ! matchValidityAnnotation ){
                                        matchValidityAnnotation = true;
                                        if ( ! Epoch.isValid(annotationValidityValue))
                                        {
                                            logger.info("Deleting pod " + namespacePod);
                                            logger.info("Pod " + namespacePod + " expired at \"" + Epoch.epochToDate(annotationValidityValue) + ". " + annotationValidityValue + " smaller than " + Epoch.dateToEpoch());
                                            V1DeleteOptions v1DeleteOptions = new V1DeleteOptions();
                                            v1DeleteOptions.setApiVersion("v1");
                                            V1Pod v1Pod = new V1Pod();
                                            v1DeleteOptions.setOrphanDependents(false);
                                            try {
                                                api.deleteNamespacedPod(
                                                        item.getMetadata().getName(),
                                                        item.getMetadata().getNamespace(),
                                                        null,
                                                        "false",
                                                        null,
                                                        null,
                                                        null,
                                                        v1DeleteOptions
                                                );

                                            } catch (Exception e) {
                                                logger.error("Check Service account permission. App may be without privilege to read or delete resources STACK=" + e);
                                            }
                                        } else {
                                            logger.info("Pod " + namespacePod + " is not expired. Will expire at " + Epoch.epochToDate(annotationValidityValue) + ". Skipping...");
                                        }
                                    }
                                }
                            }
                            if ( ! matchValidityAnnotation && ( annotationCount == numberOfAnnoations)) {
                                logger.info("Pod " + namespacePod + " does not have timebomb validity annotation. Skipping...");
                                logger.info("size=" + numberOfAnnoations.toString() + " count=" + annotationCount.toString());
                            }
                        }
                        else
                        {
                            if (( labelCount == numberOfLabels ) && ! matchLabelEnabled )
                            {
                                logger.info("Pod " + namespacePod + " does not have timebomb label enabled. Skipping...");
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            logger.error("Fail to connect to cluster kubernetes. Stack=" + String.valueOf(e.getMessage()));
        }
    }
}
