package com.fincapital.repository;

import com.fincapital.entity.Payment;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCompany_IdAndBranch_IdOrderByPaymentDateDesc(Long companyId, Long branchId);

    long countByCompany_Id(Long companyId);

    @Query("select coalesce(sum(p.paymentAmount+p.finePaid),0) from Payment p where p.company.id=:companyId and p.branch.id=:branchId and p.paymentDate>=:start and p.paymentDate<:end")
    BigDecimal sumReceivedBetween(@Param("companyId") Long companyId, @Param("branchId") Long branchId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
