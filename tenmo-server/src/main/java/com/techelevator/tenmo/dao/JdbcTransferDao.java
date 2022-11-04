package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getTransfers(int id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, amount, sender_id, receiver_id FROM transfer WHERE sender_id = ? OR receiver_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int id) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, amount, sender_id, receiver_id FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public Transfer createTransfer(BigDecimal balance, int senderId, int receiverId) {
        String sql = "INSERT into transfer (transfer_id, amount, sender_id, receiver_id)" +
                " VALUES (DEFAULT, ?, ?, ?) RETURNING transfer_id;";
        Integer id = jdbcTemplate.update(sql, Integer.class, balance, senderId, receiverId);
        return getTransfer(id);
    }

    @Override
    public void completeTransfer(Transfer transfer) {
        String sql = "BEGIN TRANSACTION;" +
                " UPDATE account SET balance = balance - ? WHERE account_id = ?;" +
                " UPDATE account SET balance = balance + ? WHERE account_id = ?;" +
                " COMMIT;";
        jdbcTemplate.queryForRowSet(sql, Transfer.class, transfer.getAmount(), transfer.getSenderId(), transfer.getAmount(), transfer.getReceiverId());
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        return new Transfer(results.getInt("transfer_id"), results.getBigDecimal("amount"), results.getInt("sender_id"), results.getInt("receiver_id"));
    }
}
