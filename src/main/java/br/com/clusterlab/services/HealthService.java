package br.com.clusterlab.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthService {
    Logger logger = LoggerFactory.getLogger(ValidationService.class);
    @GetMapping("/alive")
    public String alive()
    {
        return "health";
    }
    @GetMapping("/ready")
    public String ready()
    {
        return "health";
    }
    @GetMapping("/")
    public String health()
    {
        return "health";
    }
}
