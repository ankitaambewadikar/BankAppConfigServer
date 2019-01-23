package com.moneymoney.app.transactionsservice.service;

import java.time.LocalDate;
import java.util.List;

import com.moneymoney.app.transactionsservice.entity.Transaction;

public interface TransactionService {

	List<Transaction> getStatement(LocalDate startDate, LocalDate endDate);

	Double withdraw(int accountNumber, String transactioDetails, double currentBalance, double amount);

	Double deposit(int accountNumber, String transactioDetails, double currentBalance, double amount);

	List<Transaction> getAlltransactions();

	Double[] fundTransfer(Transaction senderTansaction, Transaction receiverTransaction);

	List<Transaction> getStatement();

}