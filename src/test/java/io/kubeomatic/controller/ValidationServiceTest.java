package io.kubeomatic.controller;

import io.kubeomatic.dto.review.*;
import io.kubeomatic.dto.review.Object;
import io.kubeomatic.service.Epoch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kubeomatic.dto.review.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ValidationServiceTest {

    public static String podAdmissionBuilder(String namespaceName, String podName, String resourceName, String actionName, List<String> containersName, Long timeDrift, String timer, String timeBombStatus){
        String uuid = String.valueOf(UUID.randomUUID());

        AdmissionReview admissionReview = new AdmissionReview();
        Request request = new Request();
        Resource resource = new Resource();
        Object object = new Object();
        Spec spec = new Spec();
        Template template = new Template();
        Metadata__1 metadata = new Metadata__1();
        Annotations__1 annotations = new Annotations__1();
        Labels__1 labels = new Labels__1();
        annotations.setKubeomaticIoTimebombTimer(timer);
        annotations.setKubeomaticIoTimebombValid(String.valueOf(Epoch.dateToEpoch() + timeDrift));
        annotations.setKubeomaticIoTimebombValidHuman(String.valueOf(Epoch.epochToDate(Epoch.dateToEpoch() + timeDrift)));
        labels.setKubeomaticIoTimebomb(timeBombStatus);
        metadata.setAnnotations(annotations);
        metadata.setLabels(labels);
        template.setMetadata(metadata);

        ArrayList<Container> containerArrayList = new ArrayList<>();

        resource.setResource(resourceName);
        for ( String container : containersName ){
            Container containerObj = new Container();
            containerObj.setName(container);
            containerArrayList.add(containerObj);
        }

        spec.setContainers(containerArrayList);
        spec.setTemplate(template);
        object.setSpec(spec);


        request.setObject(object);
        request.setResource(resource);
        request.setOperation(actionName);
        request.setUid(uuid);
        request.setDryRun(false);
        request.setNamespace(namespaceName);
        request.setName(podName);

        admissionReview.setRequest(request);

        ObjectMapper om = new ObjectMapper();

        try {
            return om.writeValueAsString(admissionReview);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
