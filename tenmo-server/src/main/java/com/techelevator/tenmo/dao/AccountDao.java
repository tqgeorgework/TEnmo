package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getBalance(int accountId);
    List<Account> getAccounts();
    void createAccount(int newUserId);
    int findAccountIdByUserId(int id);
}
