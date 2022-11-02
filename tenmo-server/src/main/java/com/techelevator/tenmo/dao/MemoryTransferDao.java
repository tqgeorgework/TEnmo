package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MemoryTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    public MemoryTransferDao() {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfers(int id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, amount, sender_id, receiver_id FROM transfer WHERE sender_id = ? OR receiver_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            mapRowToTransfer(results);
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int id) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, amount, sender_id, receiver_id FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            mapRowToTransfer(results);
        }
        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
       Transfer transfer = new Transfer(results.getInt("transfer_id"), results.getBigDecimal("amount"), results.getInt("sender_id"), results.getInt("receiver_id"));
        return transfer;
    }
}
