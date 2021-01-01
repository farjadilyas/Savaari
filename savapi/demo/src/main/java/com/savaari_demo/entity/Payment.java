package com.savaari_demo.entity;

import com.savaari_demo.database.DBHandlerFactory;

public class Payment {

    public static final int CASH = 0, CREDIT_CARD = 1;

    // Main Attributes
    int paymentID;
    double amountPaid;
    double change;
    long timestamp;
    int paymentMode;

    // Main Constructor
    public Payment() {
        paymentID = -1;
        amountPaid = 0;
        change = 0;
        timestamp = 0;
        paymentMode = Payment.CASH;
    }

    public Payment(Double amountPaid, Double change, int paymentMode) {
        this.amountPaid = amountPaid;
        this.change = change;
        this.paymentMode = paymentMode;
    }

    // -----------------------------------------------------------------------
    //                      GETTERS and SETTERS
    // -----------------------------------------------------------------------
    public void setPaymentID(int paymentID) { this.paymentID = paymentID; }
    public int getPaymentID() { return paymentID; }
    public double getAmountPaid() {
        return amountPaid;
    }
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
    public double getChange() {
        return change;
    }
    public void setChange(double change) {
        this.change = change;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public int getPaymentMode() {
        return paymentMode;
    }
    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

    public boolean record() {
        DBHandlerFactory.getInstance().createDBHandler().recordPayment(this);
        return paymentID > 0;
    }
}
