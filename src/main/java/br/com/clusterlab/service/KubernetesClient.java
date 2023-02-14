package br.com.clusterlab.service;
import com.google.gson.reflect.TypeToken;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Watch;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class KubernetesClient {
//    public static void getPods() throws IOException, ApiException {
//        // file path to your KubeConfig
//
//        String kubeConfigPath = "/extra/kubeconfig";
//
//        // loading the out-of-cluster config, a kubeconfig from file-system
//        ApiClient client =
//                ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
//
//        // set the global default api-client to the in-cluster one from above
//        Configuration.setDefaultApiClient(client);
//
//        // the CoreV1Api loads default api-client from global configuration.
//        CoreV1Api api = new CoreV1Api();
//
//        // invokes the CoreV1Api client
//        V1PodList list =
//                api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
//        for (V1Pod item : list.getItems()) {
//            System.out.println(item.getMetadata().getName());
//        }
//    }
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
                    v1DeleteOptions.setOrphanDependents(true);
//                    api.deleteNamespacedPodCall(
//                            item.getMetadata().getName(),
//                            item.getMetadata().getNamespace(),
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null,
//                            null
//                    );
                    V1Pod v1Pod = api.deleteNamespacedPod(
                            item.getMetadata().getName(),
                            item.getMetadata().getNamespace(),
                            null,
                            "false",
                            null,
                            null,
                            null,
                            v1DeleteOptions
                    );

                }

            }
        }
    }

}
