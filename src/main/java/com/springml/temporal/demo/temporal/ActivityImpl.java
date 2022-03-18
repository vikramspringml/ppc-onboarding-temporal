package com.springml.temporal.demo.temporal;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class ActivityImpl implements Activity{

    public RestTemplate restTemplate;

    public ActivityImpl(RestTemplate template) {
        restTemplate = template;
    }

    @Override
    public void step1(String processid) {
        System.out.println("step1: "+processid);
        try {
            String response = restTemplate.getForObject(
                    "https://34.149.174.177.nip.io/hello-world", String.class);
            System.out.println("response:" + response);
            sleep(5);
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
    public void step2() {
        System.out.println("step2");
        try {
            String response = restTemplate.getForObject(
                    "https://34.149.174.177.nip.io/hello-world", String.class);
            System.out.println("response:"+response);
            sleep(5);
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
    public void step3() {
        System.out.println("step3");
        try {
            String response = restTemplate.getForObject(
                    "https://34.149.174.177.nip.io/hello-world", String.class);
            System.out.println("response:"+response);
            sleep(5);
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
    public void step4() {
        System.out.println("step4");
        try {
            String response = restTemplate.getForObject(
                    "https://34.149.174.177.nip.io/hello-world", String.class);
            System.out.println("response:"+response);
            sleep(5);
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
