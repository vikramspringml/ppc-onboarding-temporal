package com.springml.temporal.demo.temporal;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface Activity {

    @ActivityMethod
    void step1(String processid);

    @ActivityMethod
    void step2();

    @ActivityMethod
    void step3();

    @ActivityMethod
    void step4();

}
