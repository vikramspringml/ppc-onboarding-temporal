package com.springml.temporal.demo.temporal;

import com.springml.temporal.demo.model.CustomerRequest;
import com.springml.temporal.demo.model.PaymentAccountRequest;
import com.springml.temporal.demo.model.PaymentResp;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.function.Supplier;

public class ShipWorkflowImpl implements ShipWorkFlow {

    Logger log = Workflow.getLogger(ShipWorkflowImpl.class);

    private final RetryOptions retryoptions = RetryOptions.newBuilder().setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(100)).setBackoffCoefficient(2).setMaximumAttempts(50000).build();
    private final ActivityOptions options = ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(30))
            .setRetryOptions(retryoptions).build();

    private final Activity activity = Workflow.newActivityStub(Activity.class, options);

    private String workflowStatus;


    private boolean paymentAccountRequested = false;


    private boolean paymentAccountSet = false;


    @Override
    public void startWorkflow(CustomerRequest customer) {
        System.out.println("startWorkflow  "+customer);
        String crid = activity.custReg(customer);
        workflowStatus = "Cust Reg Created "+crid + ", Mailer ID Pending";

        String mid = activity.mailer(customer, crid);
        workflowStatus = "Mailer ID Created " + mid + ", EPS Payment Account Pending";

        Workflow.await(() -> paymentAccountRequested == true );
        if(paymentAccountRequested == true){
            PaymentAccountRequest req = new PaymentAccountRequest();
            req.setUsername(customer.getUsername());
            req.setCrid(crid);
            PaymentResp payment = activity.paymentAccount(req);
            workflowStatus = "EPS Payment Account Created "+payment.getEpsid()+ "\n Payment Account Details Pending";
        }

        Workflow.await(() -> paymentAccountSet == true );
        workflowStatus = "Payment Account Details Completed \n- Ship Workflow Completed";

    }

    @Override
    public void createPaymentAccountSignal() {
        this.paymentAccountRequested = true;
    }


    @Override
    public String getWorkflowStatus() {
        return workflowStatus;
    }


    @Override
    public void setPaymentAccountSignal() {
        this.paymentAccountSet = true;
    }

    public static <T> T ignoreException(Supplier<T> function, T defaultValue) {
        try {
            return function.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }


}
