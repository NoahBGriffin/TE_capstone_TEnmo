package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.IllegalTransactionException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AppController {

    @Autowired
    AccountDao accountDao;

    @Autowired
    UserDao userDao;

    @Autowired
    TransferDao transferDao;

    @RequestMapping(path="/balance", method = RequestMethod.GET)
    public Balance obtainBalance(Principal principal) {

        String name = principal.getName();
        int userId = userDao.findIdByUsername(name);

        BigDecimal balance = accountDao.retrieveBalance(userId);

        Balance balanceObject = new Balance();
        balanceObject.setBalance(balance);

        return balanceObject;
    }

    @RequestMapping(path="/users", method = RequestMethod.GET)
    public List<UserWeb> getUsers() {
        return userDao.getUsers();
    }

    @RequestMapping(path="/send-money", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Transfer transferBalance(@RequestBody Transfer transfer) throws IllegalTransactionException {

        return transferDao.addTransfer(transfer);
    }

    @RequestMapping(path="/user/{id}", method = RequestMethod.GET)
    public UserWeb getUser(@PathVariable int id) throws UserNotFoundException {

        UserWeb user = userDao.getUser(id);
        int accountId = accountDao.getAccountId(id);
        user.setAccountId(accountId);

        return user;
    }

    @RequestMapping(path="/transfers/{userId}", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(@PathVariable int userId) {

        return transferDao.getAllTransfers(userId);
    }

    @RequestMapping(path="/transfer/{transferId}", method=RequestMethod.GET)
    public Transfer getTransferDetails(@PathVariable int transferId) throws TransferNotFoundException {
        return transferDao.getTransfer(transferId);
    }

}
