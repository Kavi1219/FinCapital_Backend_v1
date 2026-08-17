package com.fincapital.repository;

import com.fincapital.entity.CustomerRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRatingRepository extends JpaRepository<CustomerRating, Long> {
    Optional<CustomerRating> findByCustomer_Id(Long customerId);
}
