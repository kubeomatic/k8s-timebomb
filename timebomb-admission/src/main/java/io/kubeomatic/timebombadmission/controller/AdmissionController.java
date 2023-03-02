package io.kubeomatic.timebombadmission.controller;
import io.kubeomatic.timebombadmission.dto.response.AdmissionResponse;
import io.kubeomatic.timebombadmission.dto.review.AdmissionReview;
import io.kubeomatic.timebombadmission.service.AdmissionService;
import io.kubeomatic.timebombadmission.service.TimerNotValidException;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class AdmissionController {
    @PostMapping({"/validation"})
    public AdmissionResponse validation(@RequestBody AdmissionReview admissionReview) throws IOException {
        return AdmissionService.validateAdmissionReview(admissionReview);
    }
    @PostMapping("/mutation")
    public AdmissionResponse mutation(@RequestBody AdmissionReview admissionReview) throws IOException, TimerNotValidException {
        return AdmissionService.mutateAdmissionReview(admissionReview);
    }
}