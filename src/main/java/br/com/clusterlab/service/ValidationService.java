package br.com.clusterlab.service;

import br.com.clusterlab.controller.ValidationController;
import br.com.clusterlab.dto.review.AdmissionReview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationService {


    public static String validateCreatePods(AdmissionReview admissionReview)
    {

        Logger logger = LoggerFactory.getLogger(ValidationController.class);

        if (validateLabel(admissionReview,"br.com.clusterlab.timebomb") && validateAnnotation(admissionReview,"br.com.clusterlab.timebomb.valid")){
            return createAlowedAdmissionResponse(admissionReview);
        }
        else {
            return createDeniedAdmissionResponse(admissionReview);
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

    private static String createDeniedAdmissionResponse(AdmissionReview admissionReview) {
        return "nok";
    }

    private static String createAlowedAdmissionResponse(AdmissionReview admissionReview) {
        return "ok";
    }

    static boolean validateAnnotation(AdmissionReview admissionReview, String annotationValidity){
//        String state = podAdmissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getAnnotations().getBrComClusterlabTimebombValid();
//        System.out.println(state);
        return true;
    }
    static boolean validateLabel(AdmissionReview admissionReview, String labelEnabled){
//        return podAdmissionReview.getRequest().getObject().getMetadata().getLabels().getBrComClusterlabTimebomb().equalsIgnoreCase("enabled");
        return true;
    }
    public static String validateCreateDeployments(AdmissionReview admissionReview) {
        return "ok for deployment";
    }
}
