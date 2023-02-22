package br.com.clusterlab.controller;
import br.com.clusterlab.dto.response.AdmissionResponse;
import br.com.clusterlab.dto.review.AdmissionReview;
import br.com.clusterlab.service.AdmissionService;
import br.com.clusterlab.service.TimerNotValidException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static br.com.clusterlab.service.Epoch.dateToEpoch;


@RestController
@RequestMapping("/api")
public class AdmissionController {
    Logger logger = LoggerFactory.getLogger(AdmissionController.class);

    @PostMapping({"/validation"})
    public AdmissionResponse validation(@RequestBody AdmissionReview admissionReview) throws IOException {
        ObjectMapper om = new ObjectMapper();
        logger.info("validadtion " + om.writeValueAsString(admissionReview));
        String resourceName = admissionReview.getRequest().getResource().getResource().toString();
        boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
        boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");
        boolean validRequest = validResourcePod || validResourceDeployment;
        String operationName = admissionReview.getRequest().getOperation().toString();
        boolean validaOperation = operationName.equalsIgnoreCase("create");

        if ( ! validaOperation  ||  ! validRequest ) {
            logger.error("Operation or resource invalid Resource=" + resourceName+ " Operation=" + operationName);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
        }
        if ( validResourcePod || validResourceDeployment ){
            return AdmissionService.validateAdmissionReview(admissionReview);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
    }
    @PostMapping("/mutation")
    public AdmissionResponse mutation(@RequestBody AdmissionReview admissionReview) throws IOException, TimerNotValidException {
        ObjectMapper om = new ObjectMapper();
        logger.info("mutation " + om.writeValueAsString( admissionReview));

        String resourceName = admissionReview.getRequest().getResource().getResource().toString();
        boolean validResourcePod = resourceName.equalsIgnoreCase("pods");
        boolean validResourceDeployment = resourceName.equalsIgnoreCase("deployments");
        boolean validRequest = validResourcePod || validResourceDeployment;
        String operationName = admissionReview.getRequest().getOperation().toString();
        boolean validaOperation = operationName.equalsIgnoreCase("create");

        if ( ! validaOperation  ||  ! validRequest ) {
            logger.error("Operation or resource invalid Resource=" + resourceName+ " Operation=" + operationName);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
        }
        if ( validResourcePod || validResourceDeployment ){
            return AdmissionService.mutateAdmissionReview(admissionReview);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
    }
    @PostMapping("/test")
    public String test(@RequestBody String data) throws JsonProcessingException, TimerNotValidException {
        return String.valueOf(AdmissionService.getTimerInMinutes(data)) ;
    }
}