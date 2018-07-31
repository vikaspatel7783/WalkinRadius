package com.walkinradius.beacon.presenter;

import com.walkinradius.beacon.validator.LoginFieldsValidator;

import junit.framework.Assert;

import org.junit.Test;

public class LoginFieldsValidatorTest {

    @Test
    public void testLengthZeroForNullValue() {
        LoginFieldsValidator loginFieldsValidator = new LoginFieldsValidator();
        Assert.assertFalse(loginFieldsValidator.isLengthNonZero(null));
    }

    @Test
    public void testLengthZeroForBlankValue() {
        LoginFieldsValidator loginFieldsValidator = new LoginFieldsValidator();
        Assert.assertFalse(loginFieldsValidator.isLengthNonZero(""));
    }

    @Test
    public void testLengthNonZeroValue() {
        LoginFieldsValidator loginFieldsValidator = new LoginFieldsValidator();
        Assert.assertTrue(loginFieldsValidator.isLengthNonZero("vikas"));
    }
}