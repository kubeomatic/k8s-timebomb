package br.com.clusterlab.controller;
import br.com.clusterlab.dto.validation.pod.PodAdmissionReview;
import br.com.clusterlab.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api")
public class ValidationController {

    @PostMapping({"/validation"})
    public String pods(@RequestBody PodAdmissionReview podAdmissionReview){
        Logger logger = LoggerFactory.getLogger(ValidationController.class);
        boolean validResourcePod = podAdmissionReview.getRequest().getResource().getResource().equalsIgnoreCase("pods");
        boolean validResourceDeployment = podAdmissionReview.getRequest().getResource().getResource().equalsIgnoreCase("deployments");
        boolean validResource = validResourcePod || validResourceDeployment;
        boolean validaOperation = podAdmissionReview.getRequest().getOperation().equalsIgnoreCase("create");

        if ( ! validaOperation  ||  ! validResource ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid");
        }
        if ( validResourcePod ){
            return ValidationService.validateCreatePods(podAdmissionReview);
        }
        if ( validResourceDeployment ){
            return ValidationService.validateCreateDeployments(podAdmissionReview);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operation or resource invalid Resource=" + podAdmissionReview.getRequest().getResource().getResource().toString() + " Operation=" + podAdmissionReview.getRequest().getOperation().toString());
    }
}