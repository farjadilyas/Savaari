package com.example.savaari.ride.adapter;

public class PaymentMethodItem {
    private int paymentImage;
    private String paymentText;

    public PaymentMethodItem(int paymentImage, String paymentText) {
        this.paymentImage = paymentImage;
        this.paymentText = paymentText;
    }

    public int getPaymentImage() {
        return paymentImage;
    }

    public void setPaymentImage(int paymentImage) {
        this.paymentImage = paymentImage;
    }

    public String getPaymentText() {
        return paymentText;
    }

    public void setPaymentText(String paymentText) {
        this.paymentText = paymentText;
    }
}
