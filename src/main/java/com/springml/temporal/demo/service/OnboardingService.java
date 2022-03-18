package com.springml.temporal.demo.service;

import com.springml.temporal.demo.temporal.WorkFlow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowException;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnboardingService {

    @Autowired
    WorkflowServiceStubs workflowServiceStubs;

    @Autowired
    WorkflowClient workflowClient;

    public void startOnboarding(String id) {
        String processid = "onboarding_" + id;
        WorkFlow workflow = workflowClient.newWorkflowStub(WorkFlow.class, WorkflowOptions.newBuilder().setTaskQueue(WorkFlow.QUEUE_NAME)
                .setWorkflowId(processid).build());

        try {
            workflow.startWorkflow(processid);

        } catch (WorkflowException e) {
            // Expected
            System.out.println("Workflow Error, "+e);
        }
    }

    public String getWorkflowStatus(String id){
        WorkFlow workflow = workflowClient.newWorkflowStub(WorkFlow.class, id);
        return workflow.getWorkflowStatus();
    }
}

