package com.springml.temporal.demo.controller;


import com.springml.temporal.demo.model.CustomerRequest;
import com.springml.temporal.demo.model.PaymentAccountRequest;
import com.springml.temporal.demo.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Autowired
    OnboardingService service;

    @PostMapping("/start")
    public String createCustomer(@RequestBody CustomerRequest customer) {
        System.out.println("createCustomer "+customer);
        service.startOnboarding(customer);
        return "Customer Creation Started";
    }

    @PostMapping("/step2")
    public String createPaymentAccount(@RequestBody PaymentAccountRequest eps) {
        service.createPaymentAccount(eps);
        return "Payment Account Started";
    }

    @PostMapping("/step3")
    public String paymentMethod(@RequestBody CustomerRequest customer) {
        service.setPaymentAccount(customer.getUsername());
        return "Payment Account Started";
    }


    @GetMapping("/getwfstatus")
    public String getStatus(@RequestParam("id") String id) {
        return service.getWorkflowStatus(id);
    }

}
