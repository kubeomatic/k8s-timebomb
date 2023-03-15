package io.kubeomatic.timebombadmission.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.kubeomatic.timebombadmission.config.AppProperties;
import io.kubeomatic.timebombadmission.controller.AdmissionController;
import io.kubeomatic.timebombadmission.dto.patch.JsonPatch;
import io.kubeomatic.timebombadmission.dto.response.AdmissionResponse;
import io.kubeomatic.timebombadmission.dto.response.Response;
import io.kubeomatic.timebombadmission.dto.response.Status;
import io.kubeomatic.timebombadmission.dto.review.AdmissionReview;
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

    static ObjectMapper om = new ObjectMapper();

    private static String getAdmissionUUID(AdmissionReview admissionReview){
        return admissionReview.getRequest().getUid();
    }
    private static String getAdmissionResource(AdmissionReview admissionReview){
        return admissionReview.getRequest().getResource().getResource();
    }
    private static String getAdmissionNameSpace(AdmissionReview admissionReview){
        return admissionReview.getRequest().getNamespace();
    }
    private static String getAdmissionName(AdmissionReview admissionReview){
        return admissionReview.getRequest().getName();
    }

    public static AdmissionResponse mutateAdmissionReview(AdmissionReview admissionReview) throws IOException, TimerNotValidException {
        logger.info("Mutation AdmissionReview: UUID=\"" +
                getAdmissionUUID(admissionReview)+ "\", " +
                getAdmissionResource(admissionReview) + " " +
                getAdmissionNameSpace(admissionReview) + "/" +
                getAdmissionName(admissionReview));
        logger.debug("Mutation AdmissionReview: " + om.writeValueAsString( admissionReview));
        AdmissionService.validateResource(admissionReview.getRequest().getResource().getResource(),true);
        AdmissionService.validateOperation(admissionReview.getRequest().getOperation());
        if (validateLabel(admissionReview)){
            return getMutateAdmissionResponse(admissionReview);
        }
        else {
            return getValidatedAdmissionResponse(admissionReview, "Mutation AdmissionReview \"" + getAdmissionUUID(admissionReview) + "\" NOT Authorized, TimeBomb Label is Disabled", false, 403);
        }
    }

    public static AdmissionResponse validateAdmissionReview(AdmissionReview admissionReview) throws IOException {
        logger.info("Validation AdmissionReview: UUID=\"" +
                getAdmissionUUID(admissionReview)+ "\", " +
                getAdmissionResource(admissionReview) + " " +
                getAdmissionNameSpace(admissionReview) + "/" +
                getAdmissionName(admissionReview));
        logger.debug("Validation AdmissionReview: " + om.writeValueAsString(admissionReview));
        AdmissionService.validateResource(admissionReview.getRequest().getResource().getResource());
        AdmissionService.validateOperation(admissionReview.getRequest().getOperation());
        if (validateLabel(admissionReview) && validateAnnotation(admissionReview)){
            return getValidatedAdmissionResponse(admissionReview, "Validadtion AdmissionReview \"" + getAdmissionUUID(admissionReview) + "\" Authorized", true, 200);
        }
        else {
            return getValidatedAdmissionResponse(admissionReview, "Validadtion AdmissionReview \"" + getAdmissionUUID(admissionReview) + "\" NOT Authorized, invalid or expired validity ", false, 403);
        }
    }
    private static AdmissionResponse getMutateAdmissionResponse(AdmissionReview admissionReview) throws IOException, TimerNotValidException {
        AdmissionResponse admissionResponse = getValidatedAdmissionResponse(admissionReview, "Resource Authorized", true, 200);
        Response response = admissionResponse.getResponse();
        String timer = null;
        String resourceName = getAdmissionResource(admissionReview);
        boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
        boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");

        if (validResourcePod && !validResourceDeployment) {
            timer = admissionReview.getRequest().getObject().getMetadata().getAnnotations().getKubeomaticIoTimebombTimer();
        }
        if (validResourceDeployment && !validResourcePod) {
            timer = admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getAnnotations().getKubeomaticIoTimebombTimer();
        }
        Integer timerInMinutes = getTimerInMinutes(timer);
        Long validity = Epoch.dateToEpoch() + timerInMinutes * 60;
        List<JsonPatch> jsonPatchList = new ArrayList<>();
        jsonPatchList.add(mutatePatchAnnotation("add","/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid",Long.toString(validity)));
        jsonPatchList.add(mutatePatchAnnotation("add","/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid-human",String.valueOf(Epoch.epochToDate(validity))));
        response.setPatch(jsonPatchListToStringBase64(jsonPatchList));
        response.setPatchType(AppProperties.getProperty(AppProperties.propertyResponsePatchType));
        admissionResponse.setResponse(response);
        logger.info(om.writeValueAsString(admissionResponse));
        return admissionResponse;
    }
    private static JsonPatch mutatePatchAnnotation(String operation, String path, String value){
        JsonPatch jsonPatchValid = new JsonPatch();
        jsonPatchValid.setOp(operation);
        jsonPatchValid.setPath(path);
        jsonPatchValid.setValue(value);
        return jsonPatchValid;
    }
    private static String jsonPatchListToStringBase64(List<JsonPatch> jsonPatchList) throws JsonProcessingException {
        String stringJsonPath = om.writeValueAsString(jsonPatchList);
        return Base64.getEncoder().encodeToString(stringJsonPath.getBytes());
    }

    private static AdmissionResponse getValidatedAdmissionResponse(AdmissionReview admissionReview, String message, Boolean allowed, Integer responseCode) throws IOException {

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
            String resourceName = admissionReview.getRequest().getResource().getResource();
            boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
            boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");

            if (validResourcePod && !validResourceDeployment) {
                validityString = admissionReview.getRequest().getObject().getMetadata().getAnnotations().getKubeomaticIoTimebombValid();
            }
            if (validResourceDeployment && !validResourcePod) {
                validityString = admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getAnnotations().getKubeomaticIoTimebombValid();
            }

            assert validityString != null;
            Long validityLong = parseLong(validityString);

            return Epoch.isValid(validityLong);
        } catch (NullPointerException | NumberFormatException e){
            logger.error("UUID=\"" + getAdmissionUUID(admissionReview) + "\" Annotation  " + AppProperties.getProperty(AppProperties.propertyLabelTimebomb) + " may be empty, " + e.getMessage());
            return false;
        }
    }
    static boolean validateLabel(AdmissionReview admissionReview) {
        String resourceName = admissionReview.getRequest().getResource().getResource();
        boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
        boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");
        try {

            if (validResourcePod && !validResourceDeployment) {
                logger.debug("UUID=\"" + getAdmissionUUID(admissionReview) + "\" POD " + admissionReview.getRequest().getObject().getMetadata().getLabels().getKubeomaticIoTimebomb());
                logger.debug("UUID=\"" + getAdmissionUUID(admissionReview) + "\" LABEL " + admissionReview.getRequest().getObject().getMetadata().getLabels().getKubeomaticIoTimebomb().equalsIgnoreCase("enabled"));
                return admissionReview.getRequest().getObject().getMetadata().getLabels().getKubeomaticIoTimebomb().equalsIgnoreCase("enabled");
            } else {
                logger.debug("UUID=\"" + getAdmissionUUID(admissionReview) + "\" Deployment " + admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getLabels().getKubeomaticIoTimebomb());
                logger.debug("UUID=\"" + getAdmissionUUID(admissionReview) + "\" LABEL " + admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getLabels().getKubeomaticIoTimebomb().equalsIgnoreCase("enabled"));
                return admissionReview.getRequest().getObject().getSpec().getTemplate().getMetadata().getLabels().getKubeomaticIoTimebomb().equalsIgnoreCase("enabled");
            }
            
        } catch (NullPointerException e) {
            logger.error("UUID=\"" + getAdmissionUUID(admissionReview) + "\" Label " + AppProperties.getProperty(AppProperties.propertyLabelTimebomb) + " may be empty, " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            logger.error("UUID=\"" + getAdmissionUUID(admissionReview) + "\" Label  " + AppProperties.getProperty(AppProperties.propertyLabelTimebomb) + " may be empty, " + e.getMessage());
            return false;
        }

    }
    public static Integer getTimerInMinutes(String timer) throws TimerNotValidException {
        Integer defaultTimerInMinutes = Integer.valueOf(AppProperties.getProperty(AppProperties.propertyTimerDefault));
        try {
            char[] charTimerArray = timer.toCharArray();
            String sanitizedTimer = timer.replaceAll("[A-z]","").replaceAll("\"","");
            int integerTimer = Integer.parseInt(sanitizedTimer);
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

    public static void validateResource(String resourceName){
        boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
        boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");
        if ( ! validResourcePod && ! validResourceDeployment) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
        }
    }
    public static void validateResource(String resourceName, boolean failForPods){
        boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
        boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");
        if ( validResourcePod && failForPods)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Do not perform mutation on PODs");
        }
        else{
            if ( ! validResourcePod && ! validResourceDeployment) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
            }
        }

    }
    public static void   validateOperation(String operationName){
        boolean validaOperation = operationName.equalsIgnoreCase("create");
        if ( ! validaOperation) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
        }
    }
}
