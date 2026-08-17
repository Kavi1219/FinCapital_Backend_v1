package com.fincapital.service;

import com.fincapital.entity.*;
import com.fincapital.repository.MoneyTransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final MoneyTransactionRepository repo;
    private final CodeService codes;

    public TransactionService(MoneyTransactionRepository r, CodeService c) {
        repo = r;
        codes = c;
    }

    public MoneyTransaction create(Company company, Branch branch, String type, Long ref, Customer customer, Loan loan, BigDecimal amount, String direction, String description, Agent agent, AppUser user) {
        MoneyTransaction t = new MoneyTransaction();
        t.setCompany(company);
        t.setBranch(branch);
        t.setTransactionCode(codes.transactionCode(company.getCompanyCode(), repo.countByCompany_Id(company.getId()) + 1));
        t.setTransactionType(type);
        t.setReferenceId(ref);
        t.setCustomer(customer);
        t.setLoan(loan);
        t.setAmount(amount);
        t.setDirection(direction);
        t.setDescription(description);
        t.setCreatedByAgent(agent);
        t.setCreatedByUser(user);
        return repo.save(t);
    }
}
