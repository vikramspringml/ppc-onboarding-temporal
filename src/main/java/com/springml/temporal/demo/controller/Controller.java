package com.springml.temporal.demo.controller;


import com.springml.temporal.demo.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    OnboardingService service;

    @PostMapping("/startWorkflow")
    public String createOrder(@RequestParam("id") String id) {
        service.startOnboarding(id);
        return "Onboarding Started";
    }

    @GetMapping("/getwfstatus")
    public String getStatus(@RequestParam("id") String id) {
        return service.getWorkflowStatus(id);
    }

}
