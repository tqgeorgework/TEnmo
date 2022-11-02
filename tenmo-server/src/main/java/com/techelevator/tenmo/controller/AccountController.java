package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.MemoryAccountDao;
import com.techelevator.tenmo.dao.MemoryTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
public class AccountController {

    private AccountDao accountDao;
    private TransferDao transferDao;

    public AccountController() {
        this.accountDao = new MemoryAccountDao();
        this.transferDao = new MemoryTransferDao();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int id) {
        return accountDao.getBalance(id);
    }

    @RequestMapping(path = "/{accountId}/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getThisTransfer(@PathVariable int accountId, @PathVariable int transferId) {
        return transferDao.getTransfer(transferId);
    }

    @RequestMapping(path = "/{accountId}/transfer", method = RequestMethod.GET)
    public List<Transfer> getTransfer(@PathVariable int accountId) {
        return transferDao.getTransfers(accountId);
    }

}

