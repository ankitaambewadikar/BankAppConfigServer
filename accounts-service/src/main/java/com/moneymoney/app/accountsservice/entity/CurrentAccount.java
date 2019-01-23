package com.moneymoney.app.accountsservice.entity;

public class CurrentAccount extends Accounts {
	private Double odLimit;
	
	public CurrentAccount() {
	}

	public CurrentAccount(Integer accountNumber, String accountHolderName, Double currentBalance, Double odLimit) {
		super(accountNumber, accountHolderName, currentBalance);
		this.odLimit = odLimit;
	}

	public Double getOdLimit() {
		return odLimit;
	}

	public void setOdLimit(Double odLimit) {
		this.odLimit = odLimit;
	}

	@Override
	public String toString() {
		return "CurrentAccount [odLimit=" + odLimit + ", toString()=" + super.toString() + "]";
	}
	
	
}
