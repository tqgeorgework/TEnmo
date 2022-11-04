package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
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
        String sql = "SELECT account_id, user_id, balance FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            accounts.add(mapRowToAccount(results));
        }
        return accounts;
    }

    @Override
    public int findAccountIdByUserId(int userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?;";
        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (accountId != null) {
            return accountId;
        } else {
            return -1;
        }
    }

    @Override
    public void createAccount(int newUserId) {
        int startingBalance = 1000;
        String sql = "INSERT INTO account (user_id, balance) VALUES (?, ?)";
        jdbcTemplate.update(sql, newUserId, startingBalance);
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account(results.getInt("account_id"), results.getInt("user_id"), results.getBigDecimal("balance"));
        return account;
    }
}
