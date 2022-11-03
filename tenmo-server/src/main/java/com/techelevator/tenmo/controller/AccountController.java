package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static com.techelevator.tenmo.model.Status.APPROVED;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
public class AccountController {

    private AccountDao accountDao;
    private TransferDao transferDao;
    //private UserDao userDao;

    public AccountController(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        //this.userDao = new JdbcUserDao(new JdbcTemplate());
    }

    @RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int accountId) {
        return accountDao.getBalance(accountId);
    }

    @RequestMapping(path = "/{accountId}/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getThisTransfer(@PathVariable int accountId, @PathVariable int transferId) {
        return transferDao.getTransfer(transferId);
    }

    @RequestMapping(path = "/{accountId}/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfer(@PathVariable int accountId) {
        return transferDao.getTransfers(accountId);
    }

/*    @RequestMapping(path = "/{accountId}/transfers", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable int accountId) {
        return transferDao.getTransfers(accountId);
    }*/

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Account> getAccounts() {
        return accountDao.getAccounts();
    }

/*    @RequestMapping(path = "/create/{id}", method = RequestMethod.POST)
    public Account createAccount(@PathVariable int id) {
        return accountDao.createAccount(id);
    }*/

    @RequestMapping(path = "/{accountId}/transfers/{receiverId}", method = RequestMethod.POST)
    public Transfer createTransfer(@PathVariable int accountId, @PathVariable int receiverId,
                                 @RequestBody BigDecimal amount, Principal principal) {
        Transfer transfer;
        if (amount.compareTo(accountDao.getBalance(accountId)) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Funds");
        } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot send negative amounts");
        } else if (accountId == receiverId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot send funds to yourself");
        } else {
            transfer = transferDao.createTransfer(amount, accountId, receiverId);
            transfer.setStatus(APPROVED);
            transferDao.completeTransfer(transfer);
        }
        return transfer;
    }

    @RequestMapping (path = "/{accountId}/transfers/{transferId}", method = RequestMethod.PUT)
    public void completeTransfer() {
    }
}

