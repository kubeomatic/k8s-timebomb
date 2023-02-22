package io.kubeomatic.controller;
import io.kubeomatic.dto.response.AdmissionResponse;
import io.kubeomatic.dto.review.AdmissionReview;
import io.kubeomatic.service.AdmissionService;
import io.kubeomatic.service.TimerNotValidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api")
public class AdmissionController {
    Logger logger = LoggerFactory.getLogger(AdmissionController.class);

    @PostMapping({"/validation"})
    public AdmissionResponse validation(@RequestBody AdmissionReview admissionReview) throws IOException {
        ObjectMapper om = new ObjectMapper();
        logger.info("Validation " + om.writeValueAsString(admissionReview));
        AdmissionService.validateResource(admissionReview);
        AdmissionService.validateOperation(admissionReview);
        return AdmissionService.validateAdmissionReview(admissionReview);
    }
    @PostMapping("/mutation")
    public AdmissionResponse mutation(@RequestBody AdmissionReview admissionReview) throws IOException, TimerNotValidException {
        ObjectMapper om = new ObjectMapper();
        logger.info("mutation " + om.writeValueAsString( admissionReview));
        AdmissionService.validateResource(admissionReview);
        AdmissionService.validateOperation(admissionReview);
        return AdmissionService.mutateAdmissionReview(admissionReview);
    }
}