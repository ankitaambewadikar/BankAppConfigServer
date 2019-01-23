package com.moneymoney.app.accountsservice.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moneymoney.app.accountsservice.entity.Accounts;
import com.moneymoney.app.accountsservice.service.AccountsService;

@EnableDiscoveryClient
@RestController
@RequestMapping("/accounts")
public class AccountResource {

	@Autowired
	private AccountsService service;

	/*
	 * @PostMapping("/savings") public void createSavingsAccount(@RequestBody
	 * SavingsAccount savingsAccount) {
	 * service.createsavingsAccount(savingsAccount); }
	 */

	@GetMapping
	public ResponseEntity<List<Accounts>> getAllAccounts() {
		List<Accounts> accounts = service.getallAccounts();
		return new ResponseEntity<>(accounts, HttpStatus.OK);
	}

	@GetMapping("/{accountNumber}")
	public ResponseEntity<Accounts> getAccountbyId(@PathVariable int accountNumber) {
		Optional<Accounts> optional = service.getAccountById(accountNumber);
		Accounts account = optional.get();
		return new ResponseEntity<Accounts>(account, HttpStatus.OK);
	}

	/*
	 * @PutMapping("/savings") public void updateSavingsAccount(@RequestBody
	 * SavingsAccount accounts) { service.updateSavingsAccount(accounts); }
	 * 
	 * @PutMapping("/current") public void updateCurrentAccount(@RequestBody
	 * CurrentAccount accounts) { service.updateCurrentAccount(accounts); }
	 */
	@GetMapping("/{accountNumber}/balance")
	public ResponseEntity<Double> getCurrentBalance(@PathVariable int accountNumber) {
		Optional<Accounts> optional = service.getAccountById(accountNumber);
		double currentBalance = optional.get().getCurrentBalance();
		System.out.println(currentBalance);

		if (optional.get() == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(currentBalance, HttpStatus.OK);
	}

	@PutMapping("/{accountNumber}")
	public ResponseEntity<Accounts> updateAccountBalance(@PathVariable int accountNumber,
			@RequestParam("currentBalance") Double currentBalance) {
		Optional<Accounts> optional = service.getAccountById(accountNumber);
		Accounts accounts = optional.get();
		if (accounts == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		/* currentBalance+=accounts.getCurrentBalance(); */
		accounts.setCurrentBalance(currentBalance);
		service.updateBalance(accounts);
		return new ResponseEntity<Accounts>(accounts, HttpStatus.OK);

	}
}
