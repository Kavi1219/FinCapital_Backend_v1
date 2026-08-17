package com.fincapital.repository;

import com.fincapital.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByCompany_IdAndBranch_IdOrderByIdDesc(Long companyId, Long branchId);

    long countByCompany_Id(Long companyId);
}
