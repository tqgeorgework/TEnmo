package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.MemoryAccountDao;
import com.techelevator.tenmo.dao.MemoryTransferDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
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

}
