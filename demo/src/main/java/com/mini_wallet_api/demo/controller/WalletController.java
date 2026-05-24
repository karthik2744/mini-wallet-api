//// WalletController.java
//
//package com.mini_wallet_api.demo.controller;
//
//import com.mini_wallet_api.demo.dto.amountrequest;
//import com.mini_wallet_api.demo.dto.transactionresponse;
//import com.mini_wallet_api.demo.dto.walletresponse;
//import com.mini_wallet_api.demo.entity.wallet;
//import com.mini_wallet_api.demo.enums.transactionstatus;
//import com.mini_wallet_api.demo.repository.walletrepository;
//import com.mini_wallet_api.demo.service.walletservice;
//import com.mini_wallet_api.demo.enums.transactiontype;
//import org.springframework.security.core.Authentication;
//
//
//import jakarta.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//public class walletcontroller {
//
//    @Autowired
//    private walletservice walletService;
//
//
//    // CREATE USER
//    @PostMapping("/users")
//    public ResponseEntity<walletresponse>
//    createUser(@RequestParam String msisdn) {
//
//        wallet wallet =
//                walletService.createUser(msisdn);
//
//        walletresponse response =
//                new walletresponse();
//
//        response.setMsisdn(wallet.getMsisdn());
//
//        response.setId(wallet.getId());
//
//        response.setCreatedAt(
//                wallet.getCreatedAt()
//        );
//
//        response.setBalance(wallet.getBalance());
//
//        return new ResponseEntity<>(
//                response,
//                HttpStatus.CREATED
//        );
//    }
// //Get user
//    @GetMapping("/users/{msisdn}")
//    public ResponseEntity<walletresponse>
//    getUser(@PathVariable String msisdn) {
//
//        walletresponse response =
//                walletService.getBalance(msisdn);
//
//        return ResponseEntity.ok(response);
//    }
//
//    // GET BALANCE
//    @GetMapping("/wallets/balance/{msisdn}")
//    public ResponseEntity<walletresponse>
//    getBalance(@PathVariable String msisdn) {
//
//        walletresponse response =
//                walletService.getBalance(msisdn);
//
//        return ResponseEntity.ok(response);
//    }
//
//
//    // DEPOSIT
//    @PostMapping("/wallets/deposit/{msisdn}")
//    public ResponseEntity<walletresponse>
//    deposit(
//
//            @PathVariable String msisdn,
//
//            @Valid
//            @RequestBody
//            amountrequest request
//    ) {
//
//        return ResponseEntity.ok(
//                walletService.deposit(
//                        msisdn,
//                        request
//                )
//        );
//    }
//
//
//    // WITHDRAW
//    @PostMapping("/wallets/withdraw/{msisdn}")
//    public ResponseEntity<walletresponse>
//    withdraw(@PathVariable String msisdn,
//             @Valid
//             @RequestBody amountrequest request) {
//
//        walletresponse response =
//                walletService.withdraw(msisdn, request);
//
//        return ResponseEntity.ok(response);
//    }
//
//
//    // GET SINGLE TRANSACTION
//    @GetMapping("/transactions/{referenceId}")
//    public ResponseEntity<transactionresponse>
//    getTransaction(
//            @PathVariable String referenceId) {
//
//        transactionresponse response =
//                walletService
//                        .getTransactionByReferenceId(
//                                referenceId);
//
//        return ResponseEntity.ok(response);
//    }
//    //GET ALL USERS
//    @GetMapping("/users")
//    public ResponseEntity<List<walletresponse>>
//    getAllUsers(
//
//            @RequestParam(defaultValue = "0")
//            int page,
//
//            @RequestParam(defaultValue = "10")
//            int size
//    ) {
//
//        List<walletresponse> responses =
//                walletService.getAllUsers(
//                        page,
//                        size
//                );
//
//        return ResponseEntity.ok(responses);
//    }
//
//
////get transactions by status
//    @GetMapping("/transactions")
//    public ResponseEntity<List<transactionresponse>>
//    getTransactions(
//
//            @RequestParam(required = false)
//            transactiontype type,
//
//            @RequestParam(required = false)
//            transactionstatus status
//    ) {
//
//        return ResponseEntity.ok(
//                walletService.getTransactionsByFilters(
//                        type,
//                        status
//                )
//        );
//    }
//    //get user transactions
//   @GetMapping("/wallets/transactions")
//    public ResponseEntity<List<transactionresponse>>
//    getUserTransactions(
//
//            @PathVariable String msisdn,
//
//            @RequestParam(required = false)
//            transactiontype type,
//
//            @RequestParam(required = false)
//            transactionstatus status
//    ) {
//
//        return ResponseEntity.ok(
//                walletService.getUserTransactions(
//                        msisdn,
//                        type,
//                        status
//                )
//        );
//    }
//
//    @GetMapping("/wallets/balance")
//    public ResponseEntity<walletresponse>
//    getMyBalance(Authentication authentication) {
//
//        String mobileNumber =
//                authentication.getName();
//
//        walletresponse response =
//                walletService.getBalance(mobileNumber);
//
//        return ResponseEntity.ok(response);
//    }
//
//
//}
//


package com.mini_wallet_api.demo.controller;

import com.mini_wallet_api.demo.dto.AmountRequest;
import com.mini_wallet_api.demo.dto.TransactionResponse;
import com.mini_wallet_api.demo.dto.WalletResponse;

import com.mini_wallet_api.demo.enums.TransactionStatus;
import com.mini_wallet_api.demo.enums.TransactionType;

import com.mini_wallet_api.demo.service.WalletService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class WalletController {

    private final WalletService walletService;

    // CREATE USER
    @PostMapping("/users")
    public ResponseEntity<WalletResponse>
    createUser(@RequestParam String msisdn) {

        WalletResponse response =
                walletService.createUser(msisdn);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // GET CURRENT USER BALANCE
    @GetMapping("/wallets/balance")
    public ResponseEntity<WalletResponse>
    getBalance(Authentication authentication) {

        String mobileNumber =
                authentication.getName();

        WalletResponse response =
                walletService.getBalance(mobileNumber);

        return ResponseEntity.ok(response);
    }

    // DEPOSIT
    @PostMapping("/wallets/deposit")
    public ResponseEntity<WalletResponse>
    deposit(

            Authentication authentication,

            @Valid
            @RequestBody
            AmountRequest request
    ) {

        String mobileNumber =
                authentication.getName();

        WalletResponse response =
                walletService.deposit(
                        mobileNumber,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // WITHDRAW
    @PostMapping("/wallets/withdraw")
    public ResponseEntity<WalletResponse>
    withdraw(

            Authentication authentication,

            @Valid
            @RequestBody
            AmountRequest request
    ) {

        String mobileNumber =
                authentication.getName();

        WalletResponse response =
                walletService.withdraw(
                        mobileNumber,
                        request
                );

        return ResponseEntity.ok(response);
    }

    // GET SINGLE TRANSACTION
    @GetMapping("/transactions/{referenceId}")
    public ResponseEntity<TransactionResponse>
    getTransaction(
            @PathVariable String referenceId
    ) {

        TransactionResponse response =
                walletService
                        .getTransactionByReferenceId(
                                referenceId
                        );

        return ResponseEntity.ok(response);
    }

    // GET CURRENT USER TRANSACTIONS
    @GetMapping("/wallets/transactions")
    public ResponseEntity<List<TransactionResponse>>
    getUserTransactions(

            Authentication authentication,

            @RequestParam(required = false)
            TransactionType type,

            @RequestParam(required = false)
            TransactionStatus status
    ) {

        String mobileNumber =
                authentication.getName();

        return ResponseEntity.ok(
                walletService.getUserTransactions(
                        mobileNumber,
                        type,
                        status
                )
        );
    }

    // GET ALL USERS
    @GetMapping("/users")
    public ResponseEntity<List<WalletResponse>>
    getAllUsers(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        List<WalletResponse> responses =
                walletService.getAllUsers(
                        page,
                        size
                );

        return ResponseEntity.ok(responses);
    }

    // GET ALL TRANSACTIONS
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>>
    getTransactions(

            @RequestParam(required = false)
            TransactionType type,

            @RequestParam(required = false)
            TransactionStatus status
    ) {

        return ResponseEntity.ok(
                walletService.getTransactionsByFilters(
                        type,
                        status
                )
        );
    }

    // TEST JWT API
    @GetMapping("/test")
    public String test(Authentication authentication) {

        return "Logged in user : "
                + authentication.getName();
    }
}
