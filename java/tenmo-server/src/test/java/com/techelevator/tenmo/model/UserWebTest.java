package com.techelevator.tenmo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserWebTest {

    @Test
    public void userWeb_instantiates_correctly() {
        UserWeb userWeb = new UserWeb();
        userWeb.setId(1);
        userWeb.setUsername("user");
        userWeb.setAccountId(2);

        assertEquals(1, userWeb.getId());
        assertEquals("user", userWeb.getUsername());
        assertEquals(2, userWeb.getAccountId());
    }

}