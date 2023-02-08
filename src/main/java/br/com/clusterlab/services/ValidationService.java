package br.com.clusterlab.services;

import br.com.clusterlab.dto.response.AdmissionReview;
import br.com.clusterlab.dto.response.Response;
import br.com.clusterlab.dto.response.Status;
import br.com.clusterlab.dto.validation.pod.Container;
import br.com.clusterlab.dto.validation.pod.PodAdmissionReview;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
public class ValidationService {
    Logger logger = LoggerFactory.getLogger(ValidationService.class);
    @PostMapping({"/pods"})
    public String pods(@RequestBody PodAdmissionReview podAdmissionReview) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String uid = podAdmissionReview.getRequest().getUid();
        Status status = new Status();
        Response response = new Response();
        AdmissionReview admissionReview = new AdmissionReview();
        String resourceMatch = podAdmissionReview.getRequest().getResource().getResource();
        if ( ! "pods".equals(resourceMatch)) {
            status.setCode(403);
            status.setMessage("Resource NOT Authorized.\"");
            response.setAllowed(false);
        } else {
            status.setCode(200);
            status.setMessage("Resource Authorized");
            response.setAllowed(true);
        }

        response.setUid(uid);
        response.setStatus(status);

        admissionReview.setResponse(response);
        String responseData;
        try {
            responseData = om.writeValueAsString(admissionReview);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for (Container container : podAdmissionReview.getRequest().getObject().getSpec().getContainers())
        {
          logger.info(container.getName());
        }
        logger.info(
                "UID=" + podAdmissionReview.getRequest().getUid() +
                " Namespace=" + podAdmissionReview.getRequest().getNamespace() +
                " Name=" + podAdmissionReview.getRequest().getName() +
                " Operation=" + podAdmissionReview.getRequest().getOperation());
        logger.info("RESPONSEDATA " + responseData);
        return responseData;
    }
}