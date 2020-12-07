package com.savaari_demo.entity;

public class Payment {

    public static final int CASH = 0, CREDIT_CARD = 1;

    int paymentID;
    double amountPaid;
    double change;
    long timestamp;
    int paymentMode;

    public Payment() {
        paymentID = -1;
        amountPaid = 0;
        change = 0;
        timestamp = 0;
        paymentMode = Payment.CASH;
    }

    public void setPaymentID(int paymentID) { this.paymentID = paymentID; }

    public int getPaymentID() { return paymentID; }
}
