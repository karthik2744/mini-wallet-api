// walletcontroller.java

package com.mini_wallet_api.demo.controller;

import com.mini_wallet_api.demo.dto.amountrequest;
import com.mini_wallet_api.demo.dto.transactionresponse;
import com.mini_wallet_api.demo.dto.walletresponse;
import com.mini_wallet_api.demo.entity.wallet;
import com.mini_wallet_api.demo.service.walletservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class walletcontroller {

    @Autowired
    private walletservice walletService;


    // CREATE USER
    @PostMapping("/users")
    public ResponseEntity<walletresponse>
    createUser(@RequestParam String msisdn) {

        wallet wallet =
                walletService.createUser(msisdn);

        walletresponse response =
                new walletresponse();

        response.setMsisdn(wallet.getMsisdn());

        response.setId(wallet.getId());

        response.setCreatedAt(
                wallet.getCreatedAt()
        );

        response.setBalance(wallet.getBalance());

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }


    // GET BALANCE
    @GetMapping("/wallets/{msisdn}/balance")
    public ResponseEntity<walletresponse>
    getBalance(@PathVariable String msisdn) {

        walletresponse response =
                walletService.getBalance(msisdn);

        return ResponseEntity.ok(response);
    }


    // DEPOSIT
    @PostMapping("/wallets/{msisdn}/deposit")
    public ResponseEntity<walletresponse>
    deposit(@PathVariable String msisdn,
            @RequestBody amountrequest request) {

        walletresponse response =
                walletService.deposit(msisdn, request);

        return ResponseEntity.ok(response);
    }


    // WITHDRAW
    @PostMapping("/wallets/{msisdn}/withdraw")
    public ResponseEntity<walletresponse>
    withdraw(@PathVariable String msisdn,
             @RequestBody amountrequest request) {

        walletresponse response =
                walletService.withdraw(msisdn, request);

        return ResponseEntity.ok(response);
    }


    // GET ALL TRANSACTIONS
    @GetMapping("/wallets/{msisdn}/transactions")
    public ResponseEntity<List<transactionresponse>>
    getTransactions(@PathVariable String msisdn) {

        List<transactionresponse> responses =
                walletService.getTransactions(msisdn);

        return ResponseEntity.ok(responses);
    }


    // GET SINGLE TRANSACTION
    @GetMapping("/transactions/{referenceId}")
    public ResponseEntity<transactionresponse>
    getTransaction(
            @PathVariable String referenceId) {

        transactionresponse response =
                walletService
                        .getTransactionByReferenceId(
                                referenceId);

        return ResponseEntity.ok(response);
    }
    //GET ALL USERS
    @GetMapping("/users")
    public ResponseEntity<List<walletresponse>>
    getAllUsers() {

        List<walletresponse> responses =
                walletService.getAllUsers();

        return ResponseEntity.ok(responses);
    }

}
