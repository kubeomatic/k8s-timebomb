package io.kubeomatic.timebombscheduler.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home() throws IOException {
        return "UP";
    }
}