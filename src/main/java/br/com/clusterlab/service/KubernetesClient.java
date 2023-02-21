package br.com.clusterlab.service;
import br.com.clusterlab.controller.AdmissionController;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class KubernetesClient {
    public static Logger logger = LoggerFactory.getLogger(AdmissionController.class);
    public static void getPodsInCluster() throws IOException, ApiException {
        // loading the in-cluster config, including:
        //   1. service-account CA
        //   2. service-account bearer-token
        //   3. service-account namespace
        //   4. master endpoints(ip, port) from pre-set environment variables
        ApiClient client = ClientBuilder.cluster().build();

        // if you prefer not to refresh service account token, please use:
        // ApiClient client = ClientBuilder.oldCluster().build();

        // set the global default api-client to the in-cluster one from above
        Configuration.setDefaultApiClient(client);

        // the CoreV1Api loads default api-client from global configuration.
        CoreV1Api api = new CoreV1Api();

        // invokes the CoreV1Api client
        V1PodList list =
                api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
        for (V1Pod item : list.getItems()) {
            for (Map.Entry<String,String> label: item.getMetadata().getLabels().entrySet()){
                if (
                        (label.getKey().equals("br.com.clusterlab.timebomb")) &&
                        (label.getValue().toLowerCase().equals("enabled") )){
                    System.out.println(item.getMetadata().getNamespace() + "/" + item.getMetadata().getName());
                    System.out.println(item.getMetadata().getAnnotations().get("br.com.clusterlab.timebomb.valid"));
                    System.out.println(item.getMetadata().getAnnotations().get("br.com.clusterlab.timebomb.timer"));
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
                        logger.error("Check Service account permission. App may be without privilege to read or delete resources STACK=" + String.valueOf(e));
                    }
                }
            }
        }
    }

}
