package br.com.clusterlab.service;

import br.com.clusterlab.controller.ValidationController;
import br.com.clusterlab.dto.response.AdmissionReview;
import br.com.clusterlab.dto.response.Response;
import br.com.clusterlab.dto.response.Status;
import br.com.clusterlab.dto.validation.pod.Container;
import br.com.clusterlab.dto.validation.pod.PodAdmissionReview;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public class ValidationService {


    public static String validateCreatePods(PodAdmissionReview podAdmissionReview)
    {

        Logger logger = LoggerFactory.getLogger(ValidationController.class);

        if (validateLabel(podAdmissionReview,"br.com.clusterlab.timebomb") && validateAnnotation(podAdmissionReview,"br.com.clusterlab.timebomb.valid")){
            return createAlowedAdmissionResponse(podAdmissionReview);
        }
        else {
            return createDeniedAdmissionResponse(podAdmissionReview);
        }

//        ObjectMapper om = new ObjectMapper();
//        Status status = new Status();
//        Response response = new Response();
//        AdmissionReview admissionReview = new AdmissionReview();
//
//        String uid = podAdmissionReview.getRequest().getUid();
//
//        String resourceMatch = podAdmissionReview.getRequest().getResource().getResource();
//        if ( ! "pods".equals(resourceMatch)) {
//            status.setCode(403);
//            status.setMessage("Resource NOT Authorized.\"");
//            response.setAllowed(false);
//        } else {
//            status.setCode(200);
//            status.setMessage("Resource Authorized");
//            response.setAllowed(true);
//        }
//
//        response.setUid(uid);
//        response.setStatus(status);
//
//        admissionReview.setResponse(response);
//        String responseData;
//        try {
//            responseData = om.writeValueAsString(admissionReview);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        String podAdmissionData;
//        try {
//            podAdmissionData = om.writeValueAsString(podAdmissionReview);
//            logger.info(podAdmissionData);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        if ( admissionReview.getResponse().getAllowed())
//        {
//            if ( podAdmissionReview.getRequest().getObject() == null )
//            {
//                for (Container container : podAdmissionReview.getRequest().getOldObject().getSpec().getContainers())
//                {
//                    logger.info(container.getName());
//                }
//
//            } else
//            {
//                for (Container container : podAdmissionReview.getRequest().getObject().getSpec().getContainers())
//                {
//                    logger.info("Container " + container.getName());
//                }
//            }
//
//            logger.info(
//                    "UID=" + podAdmissionReview.getRequest().getUid() +
//                            " DRYRUN=" + podAdmissionReview.getRequest().getDryRun().toString() +
//                            " Namespace=" + podAdmissionReview.getRequest().getNamespace() +
//                            " Name=" + podAdmissionReview.getRequest().getName() +
//                            " Resource=" + podAdmissionReview.getRequest().getResource().getResource() +
//                            " Operation=" + podAdmissionReview.getRequest().getOperation());
//            logger.info("RESPONSEDATA " + responseData);
//        }
//        return "ok";
    }

    private static String createDeniedAdmissionResponse(PodAdmissionReview podAdmissionReview) {
        return "nok";
    }

    private static String createAlowedAdmissionResponse(PodAdmissionReview podAdmissionReview) {
        return "ok";
    }

    static boolean validateAnnotation(PodAdmissionReview podAdmissionReview, String annotationValidity){
        String state = podAdmissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getAnnotations().getBrComClusterlabTimebombValid();
        System.out.println(state);
        return true;
    }
    static boolean validateLabel(PodAdmissionReview podAdmissionReview, String labelEnabled){
        return podAdmissionReview.getRequest().getObject().getMetadata().getLabels().getBrComClusterlabTimebomb().equalsIgnoreCase("enabled");
    }
    public static String validateCreateDeployments(PodAdmissionReview podAdmissionReview) {
        return "ok for deployment";
    }
}
