package com.springml.temporal.demo.temporal;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface WorkFlow {

    public static final String QUEUE_NAME = "TestOnboarding";

    @WorkflowMethod
    void startWorkflow(String processid);

    @QueryMethod
    String getWorkflowStatus();


}