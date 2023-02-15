package br.com.clusterlab.controller;

import br.com.clusterlab.service.KubernetesClient;
import io.kubernetes.client.openapi.ApiException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/k8s")
public class K8s {
//    @GetMapping("/kcfg")
//    public String getPods() throws IOException, ApiException {
//        KubernetesClient.getPods();
//        return "ok";
//    }
    @GetMapping("/incluster")
    public String getPodsInCluster() throws IOException, ApiException {
        KubernetesClient.getPodsInCluster();
        return "ok";
    }
}
