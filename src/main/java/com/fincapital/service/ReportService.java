package com.fincapital.service;

import com.fincapital.dto.TransactionResponse;
import com.fincapital.entity.MoneyTransaction;
import com.fincapital.repository.MoneyTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    private final MoneyTransactionRepository repo;

    public ReportService(MoneyTransactionRepository r) {
        repo = r;
    }

    public List<TransactionResponse> overall(Long c, Long b, LocalDate f, LocalDate t) {
        return repo.findByCompany_IdAndBranch_IdAndTransactionDateBetweenOrderByTransactionDateDesc(c, b, f.atStartOfDay(), t.plusDays(1).atStartOfDay()).stream().map(this::map).toList();
    }

    private TransactionResponse map(MoneyTransaction x) {
        return new TransactionResponse(x.getId(), x.getTransactionCode(), x.getTransactionType(), x.getCustomer() == null ? null : x.getCustomer().getId(), x.getCustomer() == null ? null : x.getCustomer().getCustomerCode(), x.getLoan() == null ? null : x.getLoan().getId(), x.getLoan() == null ? null : x.getLoan().getLoanCode(), x.getAmount(), x.getDirection(), x.getDescription(), x.getTransactionDate());
    }
}
