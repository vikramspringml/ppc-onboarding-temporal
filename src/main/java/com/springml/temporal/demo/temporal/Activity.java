package com.springml.temporal.demo.temporal;

import com.springml.temporal.demo.model.CustomerRequest;
import com.springml.temporal.demo.model.PaymentAccountRequest;
import com.springml.temporal.demo.model.PaymentResp;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface Activity {

    @ActivityMethod
    String custReg(CustomerRequest customer);

    @ActivityMethod
    String mailer(CustomerRequest customer, String crid);

    @ActivityMethod
    PaymentResp paymentAccount(PaymentAccountRequest paymentAccountRequest);

}
