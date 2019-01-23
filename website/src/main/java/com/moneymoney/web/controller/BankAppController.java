package com.moneymoney.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.moneymoney.web.entity.CurrentDataSet;
import com.moneymoney.web.entity.Transaction;

@Controller
public class BankAppController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/deposit")
	public String depositForm() {
		return "DepositForm";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	public String deposit(@ModelAttribute Transaction transaction, Model model) {

		restTemplate.postForEntity("http://transactions-client/transactions", transaction, null);

		model.addAttribute("message", "Success!");
		return "DepositForm";
	}

	@RequestMapping("/withdraw")
	public String withdrawForm() {
		return "WithdrawForm";

	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public String withdraw(@ModelAttribute Transaction transaction, Model model) {

		restTemplate.postForEntity("http://transactions-client/transactions/withdraw", transaction, null);
		model.addAttribute("message", "Success!");
		return "WithdrawForm";
	}

	@RequestMapping("/transfer")
	public String fundTransferForm() {
		return "FundTransfer";
	}

	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public String fund(@RequestParam("senderAccountNumber") Integer senderAccountNumber,
			@RequestParam("amount") Double amount, @RequestParam("receiverAccountNumber") Integer receiverAccountnumber,
			Model model) {
		Transaction transaction = new Transaction();
		transaction.setAccountNumber(senderAccountNumber);
		transaction.setAmount(amount);
		transaction.setTransactionDetails("Online");
		restTemplate.postForEntity(
				"http://transactions-client/transactions/transfer?receiverAccountnumber=" + receiverAccountnumber,
				transaction, null);
		model.addAttribute("message", "Success!!!");
		return "FundTransfer";
	}

	@RequestMapping("/statement")
	public ModelAndView getStatementDeposit(@RequestParam("offset") int offset, @RequestParam("size") int size) {
		CurrentDataSet currentDataSet = restTemplate.getForObject("http://transactions-client/transactions/statement",
				CurrentDataSet.class);
		int currentSize = size == 0 ? 5 : size;
		int currentOffset = offset == 0 ? 1 : offset;
		Link next = linkTo(
				methodOn(BankAppController.class).getStatementDeposit(currentOffset + currentSize, currentSize))
						.withRel("next");
		Link previous = linkTo(
				methodOn(BankAppController.class).getStatementDeposit(currentOffset - currentOffset, currentSize))
						.withRel("previous");
		List<Transaction> currentDataSetList = new ArrayList<Transaction>();
		List<Transaction> transactions = currentDataSet.getTransactions();
		System.out.println(transactions);

		for (int i = currentOffset - 1; i < currentSize + currentOffset - 1; i++) {
			if((transactions.size()<=i && i>0) || currentOffset<1)
				break;
			Transaction transaction = transactions.get(i);
			currentDataSetList.add(transaction);
		}

		CurrentDataSet dataSet = new CurrentDataSet(currentDataSetList, next, previous);

		return new ModelAndView("statements", "currentDataSet", dataSet);
	}

}
