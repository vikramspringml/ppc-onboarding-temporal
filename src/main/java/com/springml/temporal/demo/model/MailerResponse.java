package com.springml.temporal.demo.model;

public class MailerResponse {
    private String customerMid;

    public String getCustomerMid() {
        return customerMid;
    }

    @Override
    public String toString() {
        return "MailerResponse{" +
                "customerMid='" + customerMid + '\'' +
                '}';
    }

    public void setCustomerMid(String customerMid) {
        this.customerMid = customerMid;
    }
}
