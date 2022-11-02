package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> getTransfers(int id);
    Transfer getTransfer(int id);
    Transfer createTransfer(BigDecimal balance, int senderId, int receiverId);
    void completeTransfer(Transfer transfer);
}
