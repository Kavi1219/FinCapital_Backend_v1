package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companies;
    private final BranchRepository branches;
    private final AppUserRepository users;
    private final CompanySettingRepository settings;
    private final CodeService codes;
    private final PasswordEncoder encoder;

    public CompanyService(CompanyRepository a, BranchRepository b, AppUserRepository c, CompanySettingRepository d, CodeService e, PasswordEncoder f) {
        companies = a;
        branches = b;
        users = c;
        settings = d;
        codes = e;
        encoder = f;
    }

    @Transactional
    public CompanyResponse create(CreateCompanyRequest r) {
        String base = codes.companyCode(r.companyName()), code = base;
        int i = 2;
        while (companies.existsByCompanyCode(code)) code = base + i++;
        Company c = new Company();
        c.setCompanyCode(code);
        c.setCompanyName(r.companyName());
        c.setMdName(r.mdName());
        c.setCompanyMobile(r.companyMobile());
        c.setOwnerMobile(r.ownerMobile());
        c.setCompanyEmail(r.companyEmail());
        c.setAddress(r.address());
        c = companies.save(c);
        Branch b = new Branch();
        b.setCompany(c);
        b.setBranchCode("MAIN");
        b.setBranchName(r.branchName());
        b.setLocation(r.branchLocation());
        b.setAddress(r.address());
        b = branches.save(b);
        AppUser u = new AppUser();
        u.setCompany(c);
        u.setBranch(b);
        u.setFullName(r.mdName());
        u.setMobile(r.ownerMobile());
        u.setEmail(r.companyEmail());
        u.setPasswordHash(encoder.encode(r.password()));
        u.setRole("OWNER");
        users.save(u);
        CompanySetting s = new CompanySetting();
        s.setCompany(c);
        settings.save(s);
        return new CompanyResponse(c.getId(), c.getCompanyCode(), c.getCompanyName(), c.getMdName(), c.getCompanyMobile(), c.getOwnerMobile(), c.getCompanyEmail(), c.getAddress(), c.getStatus(), b.getId());
    }

    public List<CompanyResponse> all() {
        return companies.findAll().stream().map(c -> new CompanyResponse(c.getId(), c.getCompanyCode(), c.getCompanyName(), c.getMdName(), c.getCompanyMobile(), c.getOwnerMobile(), c.getCompanyEmail(), c.getAddress(), c.getStatus(), null)).toList();
    }
}
