package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account(results.getInt("account_id"), results.getInt("user_id"), results.getBigDecimal("balance"));
        return account;
    }
}
