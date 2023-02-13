package br.com.clusterlab.controller;

import br.com.clusterlab.dto.validation.pod.*;
import br.com.clusterlab.dto.validation.pod.Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ValidationServiceTest {

    public static String podAdmissionBuilder(String namespaceName, String podName, String resourceName, String actionName, List<String> containersName){
        String uuid = String.valueOf(UUID.randomUUID());

        PodAdmissionReview podAdmissionReview = new PodAdmissionReview();
        Request request = new Request();
        Resource resource = new Resource();
        Object object = new Object();
        Spec spec = new Spec();
        ArrayList<Container> containerArrayList = new ArrayList<>();

        resource.setResource(resourceName);
        for ( String container : containersName ){
            Container containerObj = new Container();
            containerObj.setName(container);
            containerArrayList.add(containerObj);
        }

        spec.setContainers(containerArrayList);
        object.setSpec(spec);

        request.setObject(object);
        request.setResource(resource);
        request.setOperation(actionName);
        request.setUid(uuid);
        request.setDryRun(false);
        request.setNamespace(namespaceName);
        request.setName(podName);

        podAdmissionReview.setRequest(request);

        ObjectMapper om = new ObjectMapper();

        try {
            return om.writeValueAsString(podAdmissionReview);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
