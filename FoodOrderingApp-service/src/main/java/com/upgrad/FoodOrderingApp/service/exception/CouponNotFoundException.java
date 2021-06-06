package com.upgrad.FoodOrderingApp.service.exception;

/**
 * CouponNotFoundException is thrown when there is no coupon found by the name provided by the customer.
 */
public class CouponNotFoundException extends Exception {
    private final String code;
    private final String errorMessage;

    public CouponNotFoundException(final String code, final String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}



