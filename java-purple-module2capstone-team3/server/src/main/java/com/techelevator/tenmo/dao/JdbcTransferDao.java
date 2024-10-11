package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer postTransfer(String transferTo, double transferAmount, String transferFrom) {
        Transfer transfer = new Transfer();

        String sqlInsertTransfer = "INSERT INTO transfer (transfer_from, transfer_to, transfer_amount, status)" +
                "VALUES (?, ?, ?, ?) RETURNING transfer_id, transfer_from, transfer_to, transfer_amount, status;";


        try {

            SqlRowSet insertTransfer = jdbcTemplate.queryForRowSet(sqlInsertTransfer, transferFrom, transferTo, transferAmount, "approved");
            if (insertTransfer.next()) {
                transfer = mapRowToTransfer(insertTransfer);

            }
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            System.out.println("Transfer failed.");
        } catch (RestClientResponseException e) {
            if (e.getRawStatusCode() >= 500) {
                System.out.println("Server Error.");
            } else if (e.getRawStatusCode() >= 400) {
                System.out.println("Client Error");
            }
        }
        return transfer;

    }

    @Override
    public Transfer getTransferById(int transferId, String transferFrom) {

        String sql = "SELECT * FROM transfer WHERE transfer_id = ? AND (transfer_from = ? OR transfer_to = ?);";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, transferId, transferFrom, transferFrom);

        if (sqlRowSet.next()){

            return mapRowToTransfer(sqlRowSet);

        }

        return new Transfer();

    }

    @Override
    public List<Transfer> getTransfersForUser(String transferFrom) {

        List<Transfer> transfers = new ArrayList<>();

        String sqlGetAllTransfersForUser = ("SELECT * FROM transfer WHERE transfer_from = ? OR transfer_to = ?;");

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetAllTransfersForUser, transferFrom, transferFrom);

        while (sqlRowSet.next()) {
            Transfer transfer = mapRowToTransfer(sqlRowSet);
            transfers.add(transfer);
        }

        return transfers;
    }

    public Transfer mapRowToTransfer(SqlRowSet sqlRowSet) {

        Transfer transfer = new Transfer();

        transfer.setTransferId(sqlRowSet.getInt("transfer_id"));
        transfer.setTransferTo(sqlRowSet.getString("transfer_to"));
        transfer.setTransferAmount(sqlRowSet.getDouble("transfer_amount"));
        transfer.setTransferFrom(sqlRowSet.getString("transfer_from"));
        transfer.setStatus(sqlRowSet.getString("status"));

        return transfer;

    }
}
