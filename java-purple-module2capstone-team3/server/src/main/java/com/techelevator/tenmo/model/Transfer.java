package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class Transfer {

    public int transferId;

    @NotBlank
    @DecimalMin(value = ".01", message = "The field transfer amount should be greater than 0")
    public double transferAmount;

    public String transferFrom;

    @NotBlank(message = "The transferTo field should not be blank")
    public String transferTo;

    public String status = "approved";

    public Transfer() {
    }

    public Transfer(double transferAmount, String transferFrom, String transferTo) {
        this.transferAmount = transferAmount;
        this.transferFrom = transferFrom;
        this.transferTo = transferTo;
        this.status = "approved";
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
    }

    public String getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(String transferTo) {
        this.transferTo = transferTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferAmount=" + transferAmount +
                ", transferFrom='" + transferFrom + '\'' +
                ", transferTo='" + transferTo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
