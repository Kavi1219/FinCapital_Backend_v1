package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loans;
    private final LoanScheduleRepository schedules;
    private final CompanyRepository companies;
    private final BranchRepository branches;
    private final CustomerRepository customers;
    private final AgentRepository agents;
    private final AppUserRepository users;
    private final CodeService codes;
    private final TransactionService tx;

    public LoanService(LoanRepository a, LoanScheduleRepository b, CompanyRepository c, BranchRepository d, CustomerRepository e, AgentRepository f, AppUserRepository g, CodeService h, TransactionService i) {
        loans = a;
        schedules = b;
        companies = c;
        branches = d;
        customers = e;
        agents = f;
        users = g;
        codes = h;
        tx = i;
    }

    @Transactional
    public LoanResponse create(CreateLoanRequest r) {
        Company c = companies.findById(r.companyId()).orElseThrow(() -> new NotFoundException("Company not found"));
        Branch b = branches.findById(r.branchId()).orElseThrow(() -> new NotFoundException("Branch not found"));
        Customer u = customers.findById(r.customerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        String cycle = r.cycle().toUpperCase(), type = r.loanType().toUpperCase();
        if (!List.of("DAILY", "WEEKLY", "MONTHLY").contains(cycle)) throw new IllegalArgumentException("Invalid cycle");
        if (!List.of("EMI", "IO").contains(type)) throw new IllegalArgumentException("Invalid loan type");
        int duration = "EMI".equals(type) ? ("DAILY".equals(cycle) ? 100 : 10) : (r.duration() == null || r.duration() <= 0 ? 0 : r.duration());
        if (duration <= 0) throw new IllegalArgumentException("IO duration is mandatory");
        BigDecimal interest = r.loanAmount().multiply(r.interestRate()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal given = r.loanAmount().subtract(interest);
        BigDecimal collection = "IO".equals(type) ? interest : r.loanAmount().divide(BigDecimal.valueOf(duration), 2, RoundingMode.HALF_UP);

        Loan l = new Loan();
        l.setCompany(c);
        l.setBranch(b);
        l.setCustomer(u);
        l.setLoanCode(codes.loanCode(c.getCompanyCode(), loans.countByCompany_Id(c.getId()) + 1));
        l.setLoanAmount(r.loanAmount());
        l.setCycle(cycle);
        l.setLoanType(type);
        l.setInterestRate(r.interestRate());
        l.setInterestAmount(interest);
        l.setAmountGiven(given);
        l.setDuration(duration);
        l.setCollectionAmount(collection);
        l.setStartDate(r.startDate());
        l.setFinishDate(add(r.startDate(), cycle, duration));
        l.setFineEnabled(r.fineEnabled() == null ? true : r.fineEnabled());
        l.setFineAmount(r.fineAmount() == null ? BigDecimal.ZERO : r.fineAmount());
        Agent agent = null;
        AppUser user = null;
        if (r.createdByAgentId() != null) {
            agent = agents.findById(r.createdByAgentId()).orElseThrow(() -> new NotFoundException("Agent not found"));
            l.setCreatedByAgent(agent);
        }
        if (r.createdByUserId() != null) {
            user = users.findById(r.createdByUserId()).orElseThrow(() -> new NotFoundException("User not found"));
            l.setCreatedByUser(user);
        }
        l = loans.save(l);

        for (int i = 1; i <= duration; i++) {
            LoanSchedule s = new LoanSchedule();
            s.setLoan(l);
            s.setInstallmentNumber(i);
            s.setDueDate(add(r.startDate(), cycle, i));
            s.setDueAmount(collection);
            s.setStatus("PENDING");
            schedules.save(s);
        }
        tx.create(c, b, "LOAN_DISBURSEMENT", l.getId(), u, l, given, "OUT", "Loan amount given to " + u.getCustomerName(), agent, user);
        return map(l);
    }

    public List<LoanResponse> all(Long c, Long b) {
        return loans.findByCompany_IdAndBranch_IdOrderByIdDesc(c, b).stream().map(this::map).toList();
    }

    public LoanResponse one(Long id) {
        return map(loans.findById(id).orElseThrow(() -> new NotFoundException("Loan not found")));
    }

    private LocalDate add(LocalDate d, String c, int n) {
        return switch (c) {
            case "DAILY" -> d.plusDays(n);
            case "WEEKLY" -> d.plusWeeks(n);
            case "MONTHLY" -> d.plusMonths(n);
            default -> d;
        };
    }

    private LoanResponse map(Loan l) {
        return new LoanResponse(l.getId(), l.getLoanCode(), l.getCustomer().getId(), l.getCustomer().getCustomerCode(), l.getCustomer().getCustomerName(), l.getLoanAmount(), l.getCycle(), l.getLoanType(), l.getInterestRate(), l.getInterestAmount(), l.getAmountGiven(), l.getDuration(), l.getCollectionAmount(), l.getStartDate(), l.getFinishDate(), l.getFineEnabled(), l.getFineAmount(), l.getStatus());
    }
}
