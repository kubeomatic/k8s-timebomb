package io.kubeomatic.controller;
import io.kubeomatic.dto.response.AdmissionResponse;
import io.kubeomatic.dto.review.AdmissionReview;
import io.kubeomatic.service.AdmissionService;
import io.kubeomatic.service.TimerNotValidException;
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