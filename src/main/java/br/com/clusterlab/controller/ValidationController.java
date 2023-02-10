package br.com.clusterlab.controller;
import br.com.clusterlab.dto.validation.pod.PodAdmissionReview;
import br.com.clusterlab.service.ValidationService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/validate")
public class ValidationController {

    @PostMapping({"/pods"})
    public String pods(@RequestBody PodAdmissionReview podAdmissionReview){
        return ValidationService.validateCreatePods(podAdmissionReview);
    }
}