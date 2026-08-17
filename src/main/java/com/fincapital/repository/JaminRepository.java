package com.fincapital.repository;

import com.fincapital.entity.Jamin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JaminRepository extends JpaRepository<Jamin, Long> {
    Optional<Jamin> findFirstByCustomer_Id(Long customerId);
}
