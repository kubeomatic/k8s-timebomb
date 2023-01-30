package br.com.clusterlab.k8sadmissioncontroller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
public class ValidatingWebhookController {
    Logger logger = LoggerFactory.getLogger(ValidatingWebhookController.class);
    @PostMapping({"/pods","/health"})
    public String welcome(@RequestBody ObjectNode request) {
        logger.info(String.valueOf(request));
        return "Olá Mundo";
    }

}