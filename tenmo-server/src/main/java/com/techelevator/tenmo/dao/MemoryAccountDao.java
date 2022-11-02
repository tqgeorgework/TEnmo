package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MemoryAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public MemoryAccountDao() {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            accounts.add(mapRowToAccount(results));
        }
        return accounts;
    }

    @Override
    public Account createAccount(int newUserId) {
        int startingBalance = 1000;
        String sql = "INSERT INTO account (user_id, balance) VALUES (?, ?)";
        return jdbcTemplate.queryForObject(sql, Account.class, newUserId, startingBalance);
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account(results.getInt("account_id"), results.getInt("user_id"), results.getBigDecimal("balance"));
        return account;
    }
}
