package com.springml.temporal.demo.model;

public class PaymentResp {
    private String epsid;
    private String companyName;
    private String businessAddress;
    private String primaryContact;
    private String phone;

    public String getEpsid() {
        return epsid;
    }

    public void setEpsid(String epsid) {
        this.epsid = epsid;
    }

    @Override
    public String toString() {
        return "PaymentResp{" +
                "epsid='" + epsid + '\'' +
                ", companyName='" + companyName + '\'' +
                ", businessAddress='" + businessAddress + '\'' +
                ", primaryContact='" + primaryContact + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

}
