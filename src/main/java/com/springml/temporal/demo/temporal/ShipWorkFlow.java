package com.springml.temporal.demo.temporal;

import com.springml.temporal.demo.model.CustomerRequest;
import com.springml.temporal.demo.model.PaymentAccountRequest;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ShipWorkFlow {

    public static final String QUEUE_NAME = "PPCOnboarding1";

    @WorkflowMethod
    void startWorkflow(CustomerRequest customer);

    @SignalMethod
    void createPaymentAccountSignal();

    @QueryMethod
    String getWorkflowStatus();

    @SignalMethod
    void setPaymentAccountSignal();
}