package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customers;
    private final JaminRepository jamins;
    private final CustomerRatingRepository ratings;
    private final CompanyRepository companies;
    private final BranchRepository branches;
    private final AgentRepository agents;
    private final AppUserRepository users;
    private final CodeService codes;

    public CustomerService(CustomerRepository a, JaminRepository b, CustomerRatingRepository c, CompanyRepository d, BranchRepository e, AgentRepository f, AppUserRepository g, CodeService h) {
        customers = a;
        jamins = b;
        ratings = c;
        companies = d;
        branches = e;
        agents = f;
        users = g;
        codes = h;
    }

    @Transactional
    public CustomerResponse create(CreateCustomerRequest r) {
        Company c = companies.findById(r.companyId()).orElseThrow(() -> new NotFoundException("Company not found"));
        Branch b = branches.findById(r.branchId()).orElseThrow(() -> new NotFoundException("Branch not found"));
        Customer x = new Customer();
        x.setCompany(c);
        x.setBranch(b);
        x.setCustomerCode(codes.customerCode(c.getCompanyCode(), customers.countByCompany_Id(c.getId()) + 1));
        x.setCustomerName(r.name());
        x.setMobile(r.mobile());
        x.setWork(r.work());
        x.setAddress(r.address());
        x.setProfilePhotoUrl(r.profilePhotoUrl());
        x.setDocumentPhotoUrl(r.documentPhotoUrl());
        if (r.createdByAgentId() != null)
            x.setCreatedByAgent(agents.findById(r.createdByAgentId()).orElseThrow(() -> new NotFoundException("Agent not found")));
        if (r.createdByUserId() != null)
            x.setCreatedByUser(users.findById(r.createdByUserId()).orElseThrow(() -> new NotFoundException("User not found")));
        x = customers.save(x);
        JaminRequest q = r.jamin();
        Jamin j = new Jamin();
        j.setCustomer(x);
        j.setJaminName(q.name());
        j.setMobile(q.mobile());
        j.setWork(q.work());
        j.setAddress(q.address());
        j.setRelationship(q.relationship());
        j.setProfilePhotoUrl(q.profilePhotoUrl());
        j.setDocumentPhotoUrl(q.documentPhotoUrl());
        jamins.save(j);
        CustomerRating cr = new CustomerRating();
        cr.setCustomer(x);
        ratings.save(cr);
        return map(x, j);
    }

    public List<CustomerResponse> all(Long c, Long b) {
        return customers.findByCompany_IdAndBranch_IdOrderByIdDesc(c, b).stream().map(x -> map(x, jamins.findFirstByCustomer_Id(x.getId()).orElse(null))).toList();
    }

    public CustomerResponse one(Long id) {
        Customer x = customers.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        return map(x, jamins.findFirstByCustomer_Id(id).orElse(null));
    }

    private CustomerResponse map(Customer x, Jamin j) {
        return new CustomerResponse(x.getId(), x.getCompany().getId(), x.getBranch().getId(), x.getCustomerCode(), x.getCustomerName(), x.getMobile(), x.getWork(), x.getAddress(), x.getProfilePhotoUrl(), x.getDocumentPhotoUrl(), x.getStatus(), j == null ? null : j.getJaminName(), j == null ? null : j.getMobile());
    }
}
