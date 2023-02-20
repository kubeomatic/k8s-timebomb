package br.com.clusterlab.controller;
import br.com.clusterlab.dto.review.AdmissionReview;
import br.com.clusterlab.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static br.com.clusterlab.service.Epoch.dateToEpoch;
import static br.com.clusterlab.service.Epoch.epochToDate;


@RestController
@RequestMapping("/api")
public class ValidationController {

    @PostMapping({"/validation"})
    public String pods(@RequestBody AdmissionReview admissionReview){
        Logger logger = LoggerFactory.getLogger(ValidationController.class);
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
            return ValidationService.validateCreate(admissionReview);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
    }
    @GetMapping("/time")
    public String time(){
        Long epochString = 1676485977L;
        Date agora = new Date();
        return String.valueOf(epochToDate(epochString));
    }
}