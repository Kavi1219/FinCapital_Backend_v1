package com.fincapital.repository;

import com.fincapital.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCompanyCode(String companyCode);

    Optional<Company> findByCompanyCode(String companyCode);
}
