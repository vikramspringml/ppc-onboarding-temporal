package com.springml.temporal.demo.config;

import com.springml.temporal.demo.temporal.ActivityImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Configuration
public class TemporalConfig {

    private String temporalServiceAddress = "127.0.0.1:7233";

    private String temporalNamespace = "default";

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        return WorkflowServiceStubs
                .newInstance(WorkflowServiceStubsOptions.newBuilder().setTarget(temporalServiceAddress).build());
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs workflowServiceStubs) {
        return WorkflowClient.newInstance(workflowServiceStubs,
                WorkflowClientOptions.newBuilder().setNamespace(temporalNamespace).build());
    }

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }

    @Autowired
    RestTemplateBuilder builder;

    @Bean
    public ActivityImpl onboardActivities() {

        return new ActivityImpl(builder.errorHandler(new RestTemplateResponseErrorHandler())
                .build());
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}