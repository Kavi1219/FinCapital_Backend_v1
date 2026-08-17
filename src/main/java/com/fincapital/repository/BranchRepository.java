package com.fincapital.repository;

import com.fincapital.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByCompany_IdOrderById(Long companyId);

    long countByCompany_Id(Long companyId);
}
