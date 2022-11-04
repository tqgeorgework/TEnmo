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

    @RequestMapping(path = "/{accountId}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int accountId, Principal principal) {
        if (isNotAuthorized(accountId, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot access another user's balance");
        }
        return accountDao.getBalance(accountId);
    }

    @RequestMapping(path = "/{accountId}/transfers/{receiverId}/{transferId}", method = RequestMethod.GET)
    //TOdo sender can check transfers according to their own id
    // Account Id isn't doing anything
    public Transfer getThisTransfer(@PathVariable int accountId, @PathVariable int receiverId,
                                    @PathVariable int transferId, Principal principal) {
        if (isNotAuthorized(accountId, principal) && isNotAuthorized(receiverId, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot view another user's transactions");
        }
        return transferDao.getTransfer(transferId);
    }

    @RequestMapping(path = "/{accountId}/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfers(@PathVariable int accountId, Principal principal) {
        if (isNotAuthorized(accountId, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot view another user's transactions");
        }
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
                                   @RequestBody Transfer transfer, Principal principal) {
        {/*if (isNotAuthorized(accountId, principal)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot initiate a transfer for another user's account");
        } else if (transfer.getAmount().compareTo(accountDao.getBalance(accountId)) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Funds");
        } else if (transfer.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot send negative amounts");
        } else if (accountId == receiverId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot send funds to yourself");
        } else {*/
            transfer = transferDao.createTransfer(transfer.getAmount(), accountId, receiverId);
            transfer.setStatus(APPROVED);
            transferDao.completeTransfer(transfer);
        }
        return transfer;
    }

    @RequestMapping(path = "/{accountId}/transfers/{transferId}", method = RequestMethod.PUT)
    public void completeTransfer(Transfer transfer) {
        transfer.setStatus(APPROVED);
        transferDao.completeTransfer(transfer);
    }

    public boolean isNotAuthorized(int accountId, Principal principal) {
        return !(accountDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())) == accountId);
    }
}

