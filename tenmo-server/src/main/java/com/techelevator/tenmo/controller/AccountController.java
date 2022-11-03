package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
    private UserDao userDao;

    public AccountController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    //Todo
    //method is feeding wrong information to accountDao.getBalance, need to resolve so that user can input Account Id and receive expected result

    @RequestMapping(path = "/{accountId}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int accountId, Principal principal) {
        if (!(accountDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())) == accountId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot access another user's balance");
        }
        return accountDao.getBalance(accountId);
    }

    @RequestMapping(path = "/{accountId}/transfers/{receiverId}/{transferId}", method = RequestMethod.GET)
    public Transfer getThisTransfer(@PathVariable int accountId, @PathVariable int receiverId,
                                    @PathVariable int transferId) {
        return transferDao.getTransfer(transferId);
    }

    @RequestMapping(path = "/{accountId}/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfers(@PathVariable int accountId) {
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

    @RequestMapping(path = "/create/{id}", method = RequestMethod.POST)
    public Account createAccount(@PathVariable int id) {
        return accountDao.createAccount(id);
    }

    @RequestMapping(path = "/{accountId}/transfers/{receiverId}/", method = RequestMethod.POST)
    public Transfer createTransfer(@PathVariable int accountId, @PathVariable int receiverId,
                                 @RequestBody BigDecimal amount, Principal principal) {
        Transfer transfer;
        //Todo Implement principal
        // question about JSON syntax w/ transferring amount
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

//    @RequestMapping (path = "/deleteall", method = RequestMethod.DELETE)
//    public void delete() {
//        userDao.deleteEverything();
//    }

    @RequestMapping (path = "/{accountId}/transfers/{transferId}", method = RequestMethod.PUT)
    public void completeTransfer() {
    }
}

