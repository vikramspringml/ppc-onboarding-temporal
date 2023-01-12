package com.springml.temporal.demo.model;

/**
 {
 "custregid": "12345",
 "mailerid": null,
 "username": "test",
 "password": "test",
 "secqtn1": "test",
 "secqtn2": "test",
 "contactTitle": "test",
 "contactFName":"fname",
 "contactLName":"lname",
 "contactEmail":"test@usps.gov"
 }

 **/
public class CustomerRequest {
    public String getCustregid() {
        return custregid;
    }

    public void setCustregid(String custregid) {
        this.custregid = custregid;
    }

    public String getMailerid() {
        return mailerid;
    }

    public void setMailerid(String mailerid) {
        this.mailerid = mailerid;
    }

    private String custregid;
    private String mailerid;
    private String username;
    private String password;
    private String secqtn1;
    private String secqtn2;
    private String contactTitle;
    private String contactFName;
    private String contactLName;
    private String contactEmail;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecqtn1() {
        return secqtn1;
    }

    public void setSecqtn1(String secqtn1) {
        this.secqtn1 = secqtn1;
    }

    public String getSecqtn2() {
        return secqtn2;
    }

    public void setSecqtn2(String secqtn2) {
        this.secqtn2 = secqtn2;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public String getContactFName() {
        return contactFName;
    }

    public void setContactFName(String contactFName) {
        this.contactFName = contactFName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", secqtn1='" + secqtn1 + '\'' +
                ", secqtn2='" + secqtn2 + '\'' +
                ", contactTitle='" + contactTitle + '\'' +
                ", contactFName='" + contactFName + '\'' +
                ", contactLName='" + contactLName + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                '}';
    }

    public String getContactLName() {
        return contactLName;
    }

    public void setContactLName(String contactLName) {
        this.contactLName = contactLName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }



    public CustomerRequest(){
    }

}
