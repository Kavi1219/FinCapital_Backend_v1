package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    private final BranchRepository branches;
    private final CompanyRepository companies;
    private final CodeService codes;

    public BranchService(BranchRepository b, CompanyRepository c, CodeService d) {
        branches = b;
        companies = c;
        codes = d;
    }

    public BranchResponse create(CreateBranchRequest r) {
        Company c = companies.findById(r.companyId()).orElseThrow(() -> new NotFoundException("Company not found"));
        Branch b = new Branch();
        b.setCompany(c);
        b.setBranchCode(codes.branchCode(c.getCompanyCode(), branches.countByCompany_Id(c.getId()) + 1));
        b.setBranchName(r.branchName());
        b.setLocation(r.location());
        b.setAddress(r.address());
        return map(branches.save(b));
    }

    public List<BranchResponse> byCompany(Long id) {
        return branches.findByCompany_IdOrderById(id).stream().map(this::map).toList();
    }

    private BranchResponse map(Branch b) {
        return new BranchResponse(b.getId(), b.getCompany().getId(), b.getBranchCode(), b.getBranchName(), b.getLocation(), b.getAddress(), b.getStatus());
    }
}
