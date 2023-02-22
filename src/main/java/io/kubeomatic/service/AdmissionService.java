package io.kubeomatic.service;

import io.kubeomatic.config.AppProperties;
import io.kubeomatic.controller.AdmissionController;
import io.kubeomatic.dto.patch.JsonPatch;
import io.kubeomatic.dto.response.AdmissionResponse;
import io.kubeomatic.dto.response.Response;
import io.kubeomatic.dto.response.Status;
import io.kubeomatic.dto.review.AdmissionReview;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

import static java.lang.Long.parseLong;

public class AdmissionService {
    static Logger logger = LoggerFactory.getLogger(AdmissionController.class);

    public static AdmissionResponse mutateAdmissionReview(AdmissionReview admissionReview) throws IOException, TimerNotValidException {
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
    private static AdmissionResponse getMutateAdmissionResponse(AdmissionReview admissionReview) throws IOException, TimerNotValidException {
        AdmissionResponse admissionResponse = getValidatedAdmissionResponse(admissionReview, "Resource Authorized", true, 200);
        Response response = admissionResponse.getResponse();
        String timer = admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getAnnotations().getKubeomaticIoTimebombTimer();
        Integer timerInMinutes = getTimerInMinutes(timer);
        Long validity = Epoch.dateToEpoch() + timerInMinutes * 60;
        List<JsonPatch> jsonPatchList = new ArrayList<>();
        JsonPatch jsonPatchValid = new JsonPatch();
        jsonPatchValid.setOp("add");
        jsonPatchValid.setPath("/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid");
        jsonPatchValid.setValue(Long.toString(validity));
        jsonPatchList.add(jsonPatchValid);
        JsonPatch jsonPatchValidHuman = new JsonPatch();
        jsonPatchValidHuman.setOp("add");
        jsonPatchValidHuman.setPath("/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid-human");
        jsonPatchValidHuman.setValue(String.valueOf(Epoch.epochToDate(validity)));
        jsonPatchList.add(jsonPatchValidHuman);
        ObjectMapper om = new ObjectMapper();
        String stringJsonPath = om.writeValueAsString(jsonPatchList);
        response.setPatch(Base64.getEncoder().encodeToString(stringJsonPath.getBytes()));
        response.setPatchType(AppProperties.getProperty(AppProperties.propertyResponsePatchType));
        admissionResponse.setResponse(response);
        logger.info(om.writeValueAsString(admissionResponse));
        return admissionResponse;
    }

    private static AdmissionResponse getValidatedAdmissionResponse(AdmissionReview admissionReview, String message, Boolean allowed, Integer responseCode) throws IOException {
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

        admissionResponse.setApiVersion(AppProperties.getProperty(AppProperties.propertieReviewApiversion));
        admissionResponse.setKind(AppProperties.getProperty(AppProperties.propertyReviewKind));
        admissionResponse.setResponse(response);
        logger.info(om.writeValueAsString(admissionResponse));
        return admissionResponse;
    }

    static boolean validateAnnotation(AdmissionReview admissionReview){
        try{
            String validityString = null;
            String resourceName = admissionReview.getRequest().getResource().getResource().toString();
            boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
            boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");

            if (validResourcePod == true && validResourceDeployment == false) {

                validityString = admissionReview.getRequest().getObject().getMetadata().getAnnotations().getKubeomaticIoTimebombValid();
            }
            if (validResourceDeployment == true && validResourcePod == false) {
                validityString = admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getAnnotations().getKubeomaticIoTimebombValid();
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
                return admissionReview.getRequest().getObject().getMetadata().getLabels().getKubeomaticIoTimebomb().equalsIgnoreCase("enabled");
            } else {
                return admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getLabels().getKubeomaticIoTimebomb().equalsIgnoreCase("enabled");
            }
            
        } catch (NullPointerException e) {
            logger.error("Label br.com.clusterlab.timebomb may be empty, " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            logger.error("Label br.com.clusterlab.timebomb may be empty, " + e.getMessage());
            return false;
        }

    }
    public static Integer getTimerInMinutes(String timer) throws TimerNotValidException {
        Integer defaultTimerInMinutes = Integer.valueOf(AppProperties.getProperty(AppProperties.propertyTimerDefault));
        try {
            char[] charTimerArray = timer.toCharArray();
            String sanitizedTimer = timer.replaceAll("[A-z]","").replaceAll("\"","");
            int integerTimer = Integer.valueOf(sanitizedTimer);
            int numberOfLetters = 0;
            int count = 0;
            String letter = null;
            while ( count < charTimerArray.length){
                if ( String.valueOf(charTimerArray[count]).equalsIgnoreCase("s") ||
                        String.valueOf(charTimerArray[count]).equalsIgnoreCase("m")||
                        String.valueOf(charTimerArray[count]).equalsIgnoreCase("h") ||
                        String.valueOf(charTimerArray[count]).equalsIgnoreCase("d")){
                    numberOfLetters++;

                    letter = String.valueOf(charTimerArray[count]);

                }
                count++;
            }
            if (numberOfLetters > 1) {
                return defaultTimerInMinutes;
            }
            else {
                switch (letter.toLowerCase()){
                    case "s":
                        return integerTimer / 60;
                    case "m":
                        return integerTimer;
                    case "h":
                        return integerTimer * 60;
                    case "d":
                        return integerTimer * 60 * 24;
                }
            }
        } catch (Exception e){
            logger.error("Failt oconvert timer data to minutes. Timer=" + timer + " STACK=" + e.getMessage());
            return defaultTimerInMinutes;
        }
        return defaultTimerInMinutes;
    }

    public static void validateResource(AdmissionReview admissionReview){
        String resourceName = admissionReview.getRequest().getResource().getResource().toString();
        boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
        boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");
        if ( ! validResourcePod && ! validResourceDeployment) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
        }
    }
    public static void   validateOperation(AdmissionReview admissionReview){
        String operationName = admissionReview.getRequest().getOperation().toString();
        boolean validaOperation = operationName.equalsIgnoreCase("create");
        if ( ! validaOperation) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
        }
    }
}
