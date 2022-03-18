package com.springml.temporal.demo.temporal;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.function.Supplier;

public class WorkflowImpl implements WorkFlow{

    private final RetryOptions retryoptions = RetryOptions.newBuilder().setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(100)).setBackoffCoefficient(2).setMaximumAttempts(50000).build();
    private final ActivityOptions options = ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(30))
            .setRetryOptions(retryoptions).build();

    private final Activity activity = Workflow.newActivityStub(Activity.class, options);

    private String workflowStatus;


    @Override
    public void startWorkflow(String processid) {

        activity.step1(processid);
        workflowStatus = "Step 1 Completed";

        activity.step2();
        workflowStatus = "Step 2 Completed";

        activity.step3();
        workflowStatus = "Step 3 Completed";

        activity.step4();
        workflowStatus = "Step 4 Completed";
    }

    @Override
    public String getWorkflowStatus() {
        return workflowStatus;
    }

    public static <T> T ignoreException(Supplier<T> function, T defaultValue) {
        try {
            return function.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }


}
