package br.com.clusterlab.service;

import br.com.clusterlab.controller.AdmissionController;
import br.com.clusterlab.dto.patch.JsonPatch;
import br.com.clusterlab.dto.response.AdmissionResponse;
import br.com.clusterlab.dto.response.Response;
import br.com.clusterlab.dto.response.Status;
import br.com.clusterlab.dto.review.AdmissionReview;
import br.com.clusterlab.dto.review.Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.common.reflection.qual.GetClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import static java.lang.Long.parseLong;

public class AdmissionService {
    static Logger logger = LoggerFactory.getLogger(AdmissionController.class);
    static final String applicationPropertieFilePath = "application.properties";
    static final String propertieResponsePatchType = "clusterlab-com-br.kubernetes.admission.response.patch-type";
    static final String propertieReviewKind = "clusterlab-com-br.kubernetes.admission.review.kind";
    static final String propertieReviewApiversion = "clusterlab-com-br.kubernetes.admission.review.apiversion";

    public static AdmissionResponse mutateAdmissionReview(AdmissionReview admissionReview) throws IOException {
        if (validateLabel(admissionReview)){
            return getMutateAdmissionResponse(admissionReview);
        }
        else {
            return getValidatedAdmissionResponse(admissionReview, "Resource Authorized", true, 200);
        }
    }


    public static AdmissionResponse validateAdmissionReview(AdmissionReview admissionReview) throws IOException {
        if (validateLabel(admissionReview) && validateAnnotation(admissionReview)){
            return getValidatedAdmissionResponse(admissionReview, "Resource Authorized", true, 200);
        }
        else {
            return getValidatedAdmissionResponse(admissionReview, "Resource NOT Authorized, invalid or expired validity ", false, 403);
        }
    }
    private static AdmissionResponse getMutateAdmissionResponse(AdmissionReview admissionReview) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(applicationPropertieFilePath)) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdmissionResponse admissionResponse = getValidatedAdmissionResponse(admissionReview, "Resource Authorized", true, 200);
        Response response = admissionResponse.getResponse();
        List<JsonPatch> jsonPatchList = new ArrayList<>();
        JsonPatch jsonPatch = new JsonPatch();
        jsonPatch.setOp("add");
        jsonPatch.setPath("/spec/template/metadata/annotations/br.com.clusterlab.timebomb.valid");
        jsonPatch.setValue("1677685022");
        jsonPatchList.add(jsonPatch);
        ObjectMapper om = new ObjectMapper();
        String stringJsonPath = om.writeValueAsString(jsonPatchList);
        response.setPatch(Base64.getEncoder().encodeToString(stringJsonPath.getBytes()));
        response.setPatchType(properties.getProperty(propertieResponsePatchType));
        admissionResponse.setResponse(response);
        return admissionResponse;
    }

    private static AdmissionResponse getValidatedAdmissionResponse(AdmissionReview admissionReview, String message, Boolean allowed, Integer responseCode) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(applicationPropertieFilePath)) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        admissionResponse.setApiVersion(properties.getProperty(propertieReviewApiversion));
        admissionResponse.setKind(properties.getProperty(propertieReviewKind));
        admissionResponse.setResponse(response);

        return admissionResponse;
    }

    static boolean validateAnnotation(AdmissionReview admissionReview){
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
    static boolean validateLabel(AdmissionReview admissionReview) {
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
