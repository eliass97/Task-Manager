package com.example.taskmanager.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class MailValidatorTest {

    @Test
    public void validateMailTrue() {
        boolean result = MailValidator.isMail("test@gmail.com");
        Assert.assertTrue(result);
    }

    @Test
    public void validateMailFalse() {
        boolean result = MailValidator.isMail("testGmail.com");
        Assert.assertFalse(result);
    }
}
