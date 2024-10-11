package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public Transfer postTransfer(Principal principal, @RequestBody Transfer transfer) {

        if (transfer.transferTo.equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't send money to yourself");
        }
        if (transfer.transferAmount <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only transfer a positive amount");
        }

        double senderBalance = accountDao.checkBalance(principal.getName()).balance;
        double recipientBalance = accountDao.checkBalance(transfer.transferTo).balance;

        if (transfer.transferAmount > senderBalance){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }

        try {
            accountDao.updateBalance(transfer.transferTo, recipientBalance + transfer.getTransferAmount());
            accountDao.updateBalance(principal.getName(), senderBalance - transfer.getTransferAmount());

            return transferDao.postTransfer(transfer.getTransferTo(), transfer.getTransferAmount(), principal.getName());

        } catch (ResourceAccessException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        }

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfersForUser(Principal principal) {
        try {
            return transferDao.getTransfersForUser(principal.getName());
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error getting transfer for user");

        }

    }

    @RequestMapping(path = "/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId, Principal principal){

        Transfer transfer;

        try {


            transfer = transferDao.getTransferById(transferId, principal.getName());

        } catch (ResourceAccessException e){
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error getting transfer by id");

        }

        if (transfer.transferId == 0){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only view transfers you are a part of");
        } else {
            return transfer;
        }

    }
}
