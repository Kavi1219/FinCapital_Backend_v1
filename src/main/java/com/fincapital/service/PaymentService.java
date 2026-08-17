package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository payments;
    private final LoanScheduleRepository schedules;
    private final CompanyRepository companies;
    private final BranchRepository branches;
    private final CustomerRepository customers;
    private final LoanRepository loans;
    private final AgentRepository agents;
    private final AppUserRepository users;
    private final CodeService codes;
    private final TransactionService tx;

    public PaymentService(PaymentRepository a, LoanScheduleRepository b, CompanyRepository c, BranchRepository d, CustomerRepository e, LoanRepository f, AgentRepository g, AppUserRepository h, CodeService i, TransactionService j) {
        payments = a;
        schedules = b;
        companies = c;
        branches = d;
        customers = e;
        loans = f;
        agents = g;
        users = h;
        codes = i;
        tx = j;
    }

    @Transactional
    public PaymentResponse create(CreatePaymentRequest r) {
        Company c = companies.findById(r.companyId()).orElseThrow(() -> new NotFoundException("Company not found"));
        Branch b = branches.findById(r.branchId()).orElseThrow(() -> new NotFoundException("Branch not found"));
        Customer u = customers.findById(r.customerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Loan l = loans.findById(r.loanId()).orElseThrow(() -> new NotFoundException("Loan not found"));
        LoanSchedule s = r.scheduleId() == null ? null : schedules.findById(r.scheduleId()).orElseThrow(() -> new NotFoundException("Schedule not found"));
        BigDecimal fine = r.finePaid() == null ? BigDecimal.ZERO : r.finePaid();
        Agent agent = r.collectedByAgentId() == null ? null : agents.findById(r.collectedByAgentId()).orElseThrow(() -> new NotFoundException("Agent not found"));
        AppUser user = r.collectedByUserId() == null ? null : users.findById(r.collectedByUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        Payment p = new Payment();
        p.setCompany(c);
        p.setBranch(b);
        p.setPaymentCode(codes.paymentCode(c.getCompanyCode(), payments.countByCompany_Id(c.getId()) + 1));
        p.setCustomer(u);
        p.setLoan(l);
        p.setSchedule(s);
        p.setPaymentAmount(r.paymentAmount());
        p.setFinePaid(fine);
        p.setPaymentMethod(r.paymentMethod() == null ? "CASH" : r.paymentMethod().toUpperCase());
        p.setNotes(r.notes());
        p.setCollectedByAgent(agent);
        p.setCollectedByUser(user);
        p.setPaymentDate(LocalDateTime.now());
        p = payments.save(p);

        if (s != null) {
            BigDecimal paid = s.getPaidAmount().add(r.paymentAmount());
            s.setPaidAmount(paid);
            s.setFinePaid(s.getFinePaid().add(fine));
            if (paid.compareTo(s.getDueAmount()) >= 0) {
                s.setStatus("PAID");
                s.setPaidDate(LocalDate.now());
            } else if (paid.compareTo(BigDecimal.ZERO) > 0) s.setStatus("PARTIAL");
            schedules.save(s);
        }
        if (r.paymentAmount().compareTo(BigDecimal.ZERO) > 0)
            tx.create(c, b, "COLLECTION", p.getId(), u, l, r.paymentAmount(), "IN", "Collection from " + u.getCustomerName(), agent, user);
        if (fine.compareTo(BigDecimal.ZERO) > 0)
            tx.create(c, b, "FINE", p.getId(), u, l, fine, "IN", "Fine received from " + u.getCustomerName(), agent, user);
        if ("EMI".equals(l.getLoanType()) && schedules.countByLoan_IdAndStatusNot(l.getId(), "PAID") == 0) {
            l.setStatus("CLOSED");
            loans.save(l);
        }
        return map(p);
    }

    public List<PaymentResponse> all(Long c, Long b) {
        return payments.findByCompany_IdAndBranch_IdOrderByPaymentDateDesc(c, b).stream().map(this::map).toList();
    }

    private PaymentResponse map(Payment p) {
        return new PaymentResponse(p.getId(), p.getPaymentCode(), p.getCustomer().getId(), p.getCustomer().getCustomerCode(), p.getLoan().getId(), p.getLoan().getLoanCode(), p.getSchedule() == null ? null : p.getSchedule().getId(), p.getPaymentAmount(), p.getFinePaid(), p.getPaymentAmount().add(p.getFinePaid()), p.getPaymentMethod(), p.getPaymentDate());
    }
}
