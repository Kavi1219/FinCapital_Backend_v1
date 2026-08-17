package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenses;
    private final CompanyRepository companies;
    private final BranchRepository branches;
    private final AgentRepository agents;
    private final AppUserRepository users;
    private final CodeService codes;
    private final TransactionService tx;

    public ExpenseService(ExpenseRepository a, CompanyRepository b, BranchRepository c, AgentRepository d, AppUserRepository e, CodeService f, TransactionService g) {
        expenses = a;
        companies = b;
        branches = c;
        agents = d;
        users = e;
        codes = f;
        tx = g;
    }

    @Transactional
    public ExpenseResponse create(CreateExpenseRequest r) {
        Company c = companies.findById(r.companyId()).orElseThrow(() -> new NotFoundException("Company not found"));
        Branch b = branches.findById(r.branchId()).orElseThrow(() -> new NotFoundException("Branch not found"));
        Agent agent = r.createdByAgentId() == null ? null : agents.findById(r.createdByAgentId()).orElseThrow(() -> new NotFoundException("Agent not found"));
        AppUser user = r.createdByUserId() == null ? null : users.findById(r.createdByUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        Expense e = new Expense();
        e.setCompany(c);
        e.setBranch(b);
        e.setExpenseCode(codes.expenseCode(c.getCompanyCode(), expenses.countByCompany_Id(c.getId()) + 1));
        e.setPurpose(r.purpose());
        e.setAmount(r.amount());
        e.setExpenseDate(r.expenseDate() == null ? LocalDate.now() : r.expenseDate());
        e.setNotes(r.notes());
        e.setCreatedByAgent(agent);
        e.setCreatedByUser(user);
        e = expenses.save(e);
        tx.create(c, b, "EXPENSE", e.getId(), null, null, e.getAmount(), "OUT", e.getPurpose(), agent, user);
        return map(e);
    }

    public List<ExpenseResponse> all(Long c, Long b) {
        return expenses.findByCompany_IdAndBranch_IdOrderByExpenseDateDescIdDesc(c, b).stream().map(this::map).toList();
    }

    public void delete(Long id) {
        if (!expenses.existsById(id)) throw new NotFoundException("Expense not found");
        expenses.deleteById(id);
    }

    private ExpenseResponse map(Expense e) {
        return new ExpenseResponse(e.getId(), e.getExpenseCode(), e.getPurpose(), e.getAmount(), e.getExpenseDate(), e.getNotes());
    }
}
