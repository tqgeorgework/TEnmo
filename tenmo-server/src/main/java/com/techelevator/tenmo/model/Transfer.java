package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.techelevator.tenmo.model.Status.*;

public class Transfer {
    @NotNull
    @JsonIgnore
    private int transferId;
    @NotNull
    @JsonFormat (shape = JsonFormat.Shape.STRING)
    private BigDecimal amount;
    @NotNull
    private int senderId;
    @NotNull
    private int receiverId;
    @JsonIgnore
    private Enum<Status> status;

    public Transfer(int transferId, BigDecimal amount, int senderId, int receiverId) {
        this.transferId = transferId;
        this.amount = amount;
        this.senderId = senderId;
        this.receiverId = receiverId;
        status = PENDING;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public Enum<Status> getStatus() {
        return status;
    }

    public void setStatus(Enum<Status> status) {
        this.status = status;
    }
}
