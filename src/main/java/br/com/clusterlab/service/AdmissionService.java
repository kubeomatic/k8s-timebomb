package br.com.clusterlab.service;

import br.com.clusterlab.controller.AdmissionController;
import br.com.clusterlab.dto.response.AdmissionResponse;
import br.com.clusterlab.dto.response.Response;
import br.com.clusterlab.dto.response.Status;
import br.com.clusterlab.dto.review.AdmissionReview;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Long.parseLong;

public class AdmissionService {
    static Logger logger = LoggerFactory.getLogger(AdmissionController.class);

    public static AdmissionResponse mutateAdmissionReview(AdmissionReview admissionReview){
        if (validateLabel(admissionReview,"br.com.clusterlab.timebomb")){
            return getMutateAdmissionResponse(admissionReview);
        }
        else {
            return getValidatedAdmissionResponse(admissionReview, "Resource Authorized", true, 200);
        }
    }


    public static AdmissionResponse validateAdmissionReview(AdmissionReview admissionReview)
    {
        if (validateLabel(admissionReview,"br.com.clusterlab.timebomb") && validateAnnotation(admissionReview,"br.com.clusterlab.timebomb.valid")){
            return getValidatedAdmissionResponse(admissionReview, "Resource Authorized", true, 200);
        }
        else {
            return getValidatedAdmissionResponse(admissionReview, "Resource NOT Authorized, invalid or expired validity ", false, 403);
        }
    }
    private static AdmissionResponse getMutateAdmissionResponse(AdmissionReview admissionReview){
        AdmissionResponse admissionResponse = getValidatedAdmissionResponse(admissionReview, "Resource Authorized", true, 200);
        Response response = admissionResponse.getResponse();
        response.setPatch("W3sib3AiOiAiYWRkIiwgInBhdGgiOiAiL3NwZWMvcmVwbGljYXMiLCAidmFsdWUiOiAzfV0=");
        response.setPatchType("JSONPatch");
        admissionResponse.setResponse(response);
        return admissionResponse;
    }

    private static AdmissionResponse getValidatedAdmissionResponse(AdmissionReview admissionReview, String message, Boolean allowed, Integer responseCode) {
        ObjectMapper om = new ObjectMapper();
        Status status = new Status();
        Response response = new Response();
        AdmissionResponse admissionResponse = new AdmissionResponse();

        String uid = admissionReview.getRequest().getUid();

        status.setCode(responseCode);
        status.setMessage(message);
        response.setAllowed(allowed);

        response.setUid(uid);
        response.setStatus(status);

        admissionResponse.setApiVersion("admission.k8s.io/v1");
        admissionResponse.setKind("AdmissionReview");
        admissionResponse.setResponse(response);

        return admissionResponse;
    }

    static boolean validateAnnotation(AdmissionReview admissionReview, String annotationValidity){
        try{
            String validityString = null;
            String resourceName = admissionReview.getRequest().getResource().getResource().toString();
            boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
            boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");

            if (validResourcePod == true && validResourceDeployment == false) {

                validityString = admissionReview.getRequest().getObject().getMetadata().getAnnotations().getBrComClusterlabTimebombValid();
            }
            if (validResourceDeployment == true && validResourcePod == false) {
                validityString = admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getAnnotations().getBrComClusterlabTimebombValid();
            }

            Long validityLong = parseLong(validityString);
            if ( validityLong < Epoch.dateToEpoch()){
                logger.info(validityString + " < " + Epoch.dateToEpoch());
                return false;
            } else {
                logger.info(validityString + " <= " + Epoch.dateToEpoch());
                return true;
            }
        } catch (NullPointerException e){
            logger.error("Annotation br.com.clusterlab.timebomb.valid may be empty, " + e.getMessage());
            return false;
        } catch (NumberFormatException e){
            logger.error("Annotation br.com.clusterlab.timebomb.valid may be empty, " + e.getMessage());
            return false;
        }

    }
    static boolean validateLabel(AdmissionReview admissionReview, String labelEnabled) {
        String resourceName = admissionReview.getRequest().getResource().getResource().toString();
        boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
        boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");
        try {

            if (validResourcePod == true && validResourceDeployment == false) {
                return admissionReview.getRequest().getObject().getMetadata().getLabels().getBrComClusterlabTimebomb().equalsIgnoreCase("enabled");
            } else {
                return admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getLabels().getBrComClusterlabTimebomb().equalsIgnoreCase("enabled");
            }
            
        } catch (NullPointerException e) {
            logger.error("Label br.com.clusterlab.timebomb may be empty, " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            logger.error("Label br.com.clusterlab.timebomb may be empty, " + e.getMessage());
            return false;
        }

    }

}
