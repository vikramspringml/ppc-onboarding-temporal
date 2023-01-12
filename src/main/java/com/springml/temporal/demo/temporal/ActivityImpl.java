package com.springml.temporal.demo.temporal;

import com.springml.temporal.demo.model.*;
import com.springml.temporal.demo.service.RestTemplateHelper;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


public class ActivityImpl implements Activity{

    Logger log = Workflow.getLogger(ActivityImpl.class);

    public RestTemplate restTemplate;
    public RestTemplateHelper restTemplateHelper;


    public ActivityImpl(RestTemplate template)
    {
        restTemplate = template;
    }


    public String custRegHelper(CustomerRequest customer) {
//        String url = "https://34.149.174.177.nip.io/v1/crid/customer";
//        String url = "https://us-central1-cbregcpsandbox.cloudfunctions.net/custreg";
        String url = "http://localhost:8081/api/crid";
        System.out.println("custReg: "+customer.getUsername());
        System.out.println("custReg URL"+ url);
        try {
            CustomerResponse resp = restTemplateHelper.postForEntity(CustomerResponse.class, url, customer);
            return resp.getCrid();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<CustomerRequest> request = new HttpEntity<>(customer, headers);
//
//            ResponseEntity<CustomerResponse> response = restTemplate.postForEntity(
//                    new URI(url), request, CustomerResponse.class);
//            System.out.println("custreg response:" + response);
//            return response.getBody().getCrid();
        } catch (RestClientException e) {
            // Get the activity type
            String type = io.temporal.activity.Activity.getExecutionContext().getInfo().getActivityType();
            // Get the retry attempt
            int attempt = io.temporal.activity.Activity.getExecutionContext().getInfo().getAttempt();
            // Wrap checked exception and throw
            throw io.temporal.activity.Activity.wrap(
                    new Exception("Activity type: " + type + " attempt times: " + attempt, e));
        }
//        catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    @Override
    public String custReg(CustomerRequest customer) {
//        String url = "https://34.149.174.177.nip.io/v1/crid/customer";
//        String url = "https://us-central1-cbregcpsandbox.cloudfunctions.net/custreg";
        String url = "http://localhost:8081/api/crid";
        System.out.println("custReg: "+customer.getUsername());
        System.out.println("custReg URL"+ url);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CustomerRequest> request = new HttpEntity<>(customer, headers);

            ResponseEntity<CustomerResponse> response = restTemplate.postForEntity(
                    new URI(url), request, CustomerResponse.class);
            System.out.println("custreg response:" + response);
            return response.getBody().getCrid();
        } catch (RestClientException e) {
            // Get the activity type
            String type = io.temporal.activity.Activity.getExecutionContext().getInfo().getActivityType();
            // Get the retry attempt
            int attempt = io.temporal.activity.Activity.getExecutionContext().getInfo().getAttempt();
            // Wrap checked exception and throw
            throw io.temporal.activity.Activity.wrap(
                    new Exception("Activity type: " + type + " attempt times: " + attempt, e));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String mailer(CustomerRequest customer, String crid) {
        System.out.println("mailer");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CustomerRequest> request = new HttpEntity<>(customer, headers);
            ResponseEntity<MailerResponse> response = restTemplate.postForEntity(
                    "http://localhost:8081/api/mid", request,
                    MailerResponse.class);
            System.out.println("mailer:"+response);
            return response.getBody().getCustomerMid();
        } catch (RestClientException e) {
            // Get the activity type
            String type = io.temporal.activity.Activity.getExecutionContext().getInfo().getActivityType();
            // Get the retry attempt
            int attempt = io.temporal.activity.Activity.getExecutionContext().getInfo().getAttempt();
            // Wrap checked exception and throw
            throw io.temporal.activity.Activity.wrap(
                    new Exception("Activity type: " + type + " attempt times: " + attempt, e));
        }
    }

    @Override
    public PaymentResp paymentAccount(PaymentAccountRequest paymentAccountRequest) {
        System.out.println("paymentAccountRequest :"+paymentAccountRequest);
        try {
            HashMap queryParams = new HashMap();
            queryParams.put("username", paymentAccountRequest.getUsername());
            queryParams.put("crid", paymentAccountRequest.getCrid());


            PaymentResp response = restTemplate.getForObject(
                    "http://localhost:8081/api/payment?"+
                            "username={username}&crid={crid}", PaymentResp.class, queryParams);
            System.out.println(response);
            return response;
        } catch (RestClientException e) {
            // Get the activity type
            String type = io.temporal.activity.Activity.getExecutionContext().getInfo().getActivityType();
            // Get the retry attempt
            int attempt = io.temporal.activity.Activity.getExecutionContext().getInfo().getAttempt();
            // Wrap checked exception and throw
            throw io.temporal.activity.Activity.wrap(
                    new Exception("Activity type: " + type + " attempt times: " + attempt, e));
        }
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep((long) seconds * 10L);
        } catch (InterruptedException ee) {
            throw io.temporal.activity.Activity.wrap(
                    new Exception(ee));
        }
    }

}
