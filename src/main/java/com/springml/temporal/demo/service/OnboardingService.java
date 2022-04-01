package com.springml.temporal.demo.service;

import com.springml.temporal.demo.model.CustomerRequest;
import com.springml.temporal.demo.model.PaymentAccountRequest;
import com.springml.temporal.demo.temporal.ShipWorkFlow;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.workflow.Async;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnboardingService {

    @Autowired
    WorkflowServiceStubs workflowServiceStubs;

    @Autowired
    WorkflowClient workflowClient;

    public void startOnboarding(CustomerRequest customer) {
        System.out.println("startOnboarding "+customer);
        String processid =  customer.getUsername();
        ShipWorkFlow workflow = workflowClient.newWorkflowStub(ShipWorkFlow.class,
                WorkflowOptions.newBuilder().setTaskQueue(ShipWorkFlow.QUEUE_NAME)
                .setWorkflowId(processid).build());
        WorkflowExecution we = WorkflowClient.start(workflow::startWorkflow, customer);
        System.out.printf("\nWorkflowID: %s RunID: %s", we.getWorkflowId(), we.getRunId());

    }

    public String getWorkflowStatus(String id){
        ShipWorkFlow workflow = workflowClient.newWorkflowStub(ShipWorkFlow.class, id);
        return workflow.getWorkflowStatus();
    }

    public void createPaymentAccount(PaymentAccountRequest request) {
        String processid =  request.getUsername();
        ShipWorkFlow workflow = workflowClient.newWorkflowStub(ShipWorkFlow.class, processid);
        workflow.createPaymentAccountSignal();

    }

    public void setPaymentAccount(String userid) {
        ShipWorkFlow workflow = workflowClient.newWorkflowStub(ShipWorkFlow.class, userid);
        workflow.setPaymentAccountSignal();
    }
}

