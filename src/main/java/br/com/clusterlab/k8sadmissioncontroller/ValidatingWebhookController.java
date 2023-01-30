package br.com.clusterlab.k8sadmissioncontroller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
public class ValidatingWebhookController {
    Logger logger = LoggerFactory.getLogger(ValidatingWebhookController.class);
    @PostMapping({"/pods"})
    public String pods(@RequestBody ObjectNode request) {
        logger.info(String.valueOf(request));
        return "Validating pods";
    }
    @PostMapping({"/health"})
    public String health(@RequestBody ObjectNode request) {
        logger.info(String.valueOf(request));
        return "Validating health";
    }

}