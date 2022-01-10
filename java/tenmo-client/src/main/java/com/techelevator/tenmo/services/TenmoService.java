package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.apiguardian.api.API;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TenmoService {

    // properties:
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    private final String API_BASE_URL = "http://localhost:8080/";

    // setter:
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    // method:
    public BigDecimal retrieveBalance() {

        Balance balance = restTemplate.exchange(API_BASE_URL + "balance", HttpMethod.GET,
                makeAuthEntity(), Balance.class).getBody();
        return balance.getBalance();
    }

    public User[] retrieveUsers() {
        User[] users = restTemplate.exchange(
                API_BASE_URL + "users",
                HttpMethod.GET,
                makeAuthEntity(),
                User[].class
        ).getBody();
        return users;
    }

    public User retrieveUser(int id) {
        User user = null;
        try {
            user = restTemplate.exchange(
                    API_BASE_URL + "user/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    User.class
            ).getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            System.out.println("Exception encountered: User not found");
        }
        return user;
    }

    public Transfer sendMoney(Transfer transfer) {

        Transfer newTransfer = null;

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "send-money",
                    HttpMethod.POST,
                    makeTransferEntity(transfer),
                    Transfer.class);
            newTransfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Exception encountered: " + e.getMessage());
        }

        return newTransfer;
    }

    public Transfer[] displayAllTransfers(int userId) {

        Transfer[] allTransfers = null;

        try {
            allTransfers = restTemplate.exchange(
                    API_BASE_URL + "transfers/" + userId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class
            ).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Exception encountered: " + e.getMessage());
        }
        return allTransfers;
    }

    public Transfer getTransfer(int transferId) {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(
                    API_BASE_URL +"transfer/" + transferId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer.class
            ).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Exception encountered: Transfer not found");
        }
        return transfer;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<Transfer>(transfer, headers);
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.authToken);
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }

}
