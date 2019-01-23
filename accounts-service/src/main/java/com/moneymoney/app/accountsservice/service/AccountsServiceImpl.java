package com.moneymoney.app.accountsservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moneymoney.app.accountsservice.entity.Accounts;
import com.moneymoney.app.accountsservice.entity.CurrentAccount;
import com.moneymoney.app.accountsservice.entity.SavingsAccount;
import com.moneymoney.app.accountsservice.repository.AccountsRepository;

@Service
public class AccountsServiceImpl implements AccountsService {

	@Autowired
	private AccountsRepository repository;

	@Override
	public List<Accounts> getallAccounts() {
		return  repository.findAll();	
	}


	@Override
	public Optional<Accounts> getAccountById(int accountNumber) {
		return repository.findById(accountNumber);
	}


	@Override
	public void updateSavingsAccount(SavingsAccount accounts) {
		repository.save(accounts);
	}


	@Override
	public void updateCurrentAccount(CurrentAccount accounts) {
		repository.save(accounts);
	}


	@Override
	public void updateBalance(Accounts accounts) {
		repository.save(accounts);
	}
	
	/*
	 * @Override public void createsavingsAccount(SavingsAccount savingsAccount) {
	 * repository.save(savingsAccount); }
	 */
	

}
