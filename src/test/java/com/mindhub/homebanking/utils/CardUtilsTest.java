package com.mindhub.homebanking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CardUtilsTest {

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberHasCorrectFormat() {
        String cardNumber = CardUtils.getCardNumber();
        assertTrue(cardNumber.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}"));
    }

    @Test
    public void cvvIsCreated() {
        int cvv = CardUtils.getCVV();
        assertThat(cvv,is(notNullValue()));
    }
    @Test
    public void cvvHasCorrectLength() {
        int cvv = CardUtils.getCVV();
        String cvvString = String.valueOf(cvv);
        assertThat(cvvString.length(), is(3));
    }

}