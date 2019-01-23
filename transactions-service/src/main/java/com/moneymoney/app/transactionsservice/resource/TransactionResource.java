package com.moneymoney.app.transactionsservice.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.moneymoney.app.transactionsservice.entity.Transaction;
import com.moneymoney.app.transactionsservice.service.TransactionService;

@EnableDiscoveryClient
@RestController
@RequestMapping("/transactions")
public class TransactionResource {

	@Autowired
	private TransactionService service;
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping
	public List<Transaction> getAllTransactions() {

		return service.getAlltransactions();

	}

	@PostMapping
	public ResponseEntity<Transaction> deposit(@RequestBody Transaction transaction) {
		
		System.out.println("start transaction");
		ResponseEntity<Double> entity = restTemplate.getForEntity(
				"http://accountService-client/accounts/" + transaction.getAccountNumber() + "/balance", Double.class);
		
		System.out.println("inside transaction");
		Double currentBalance = entity.getBody();
		Double updateBalance = service.deposit(transaction.getAccountNumber(), transaction.getTransactionDetails(),
				currentBalance, transaction.getAmount());
		restTemplate.put(
				"http://accountService-client/accounts/" + transaction.getAccountNumber() + "?currentBalance=" + updateBalance,
				null);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/withdraw")
	public ResponseEntity<Transaction> withdraw(@RequestBody Transaction transaction) {
		ResponseEntity<Double> entity = restTemplate.getForEntity(
				"http://accountService-client/accounts/" + transaction.getAccountNumber() + "/balance", Double.class);
		Double currentBalance = entity.getBody();
		Double updateBalance = service.withdraw(transaction.getAccountNumber(), transaction.getTransactionDetails(),
				currentBalance, transaction.getAmount());
		restTemplate.put(
				"http://accountService-client/accounts/" + transaction.getAccountNumber() + "?currentBalance=" + updateBalance,
				null);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/transfer")
	public ResponseEntity<Transaction> fundTransfer(@RequestBody Transaction senderTansaction,
			@RequestParam Integer receiverAccountnumber) {
		Double[] updatedBalance = new Double[2];
		System.out.println(receiverAccountnumber);
		Transaction receiverTransaction = new Transaction();
		ResponseEntity<Double> senderEntity = restTemplate.getForEntity(
				"http://accountService-client/accounts/" + senderTansaction.getAccountNumber() + "/balance", Double.class);
		Double senderCurrentbalance = senderEntity.getBody();
		ResponseEntity<Double> receiverEntity = restTemplate
				.getForEntity("http://accountService-client/accounts/" + +receiverAccountnumber + "/balance", Double.class);
		Double receivercurrentbalance = receiverEntity.getBody();
		senderTansaction.setCurrentBalance(senderCurrentbalance);
		receiverTransaction.setAccountNumber(receiverAccountnumber);
		receiverTransaction.setCurrentBalance(receivercurrentbalance);
		updatedBalance = service.fundTransfer(senderTansaction, receiverTransaction);

		System.out.println(updatedBalance);
		restTemplate.put("http://accountService-client/accounts/" + senderTansaction.getAccountNumber() + "?currentBalance="
				+ updatedBalance[0], null);
		restTemplate.put("http://accountService-client/accounts/" + receiverTransaction.getAccountNumber() + "?currentBalance="
				+ updatedBalance[1], null);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@GetMapping("/statement")
	public ResponseEntity<CurrentDataSet> getStatement() {
		CurrentDataSet currentDataSet = new CurrentDataSet();
		List<Transaction> transactions = service.getStatement();
		currentDataSet.setTransactions(transactions);

		return new ResponseEntity<>(currentDataSet, HttpStatus.OK);

	}
}
