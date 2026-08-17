package com.fincapital.repository;

import com.fincapital.entity.LoanSchedule;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LoanScheduleRepository extends JpaRepository<LoanSchedule, Long> {
    List<LoanSchedule> findByLoan_IdOrderByInstallmentNumber(Long loanId);

    long countByLoan_IdAndStatusNot(Long loanId, String status);

    @Query("select coalesce(sum(s.dueAmount),0) from LoanSchedule s where s.loan.company.id=:companyId and s.loan.branch.id=:branchId and s.dueDate=:date")
    BigDecimal sumDueForDate(@Param("companyId") Long companyId, @Param("branchId") Long branchId, @Param("date") LocalDate date);

    @Query("select coalesce(sum((s.dueAmount-s.paidAmount)+(s.fineAmount-s.finePaid)),0) from LoanSchedule s where s.loan.company.id=:companyId and s.loan.branch.id=:branchId and s.dueDate<=:date and s.status in ('PENDING','PARTIAL','OVERDUE')")
    BigDecimal sumPendingOverdue(@Param("companyId") Long companyId, @Param("branchId") Long branchId, @Param("date") LocalDate date);

    @Query("select coalesce(sum(s.dueAmount-s.paidAmount),0) from LoanSchedule s where s.loan.company.id=:companyId and s.loan.branch.id=:branchId and s.dueDate between :fromDate and :toDate and s.status in ('PENDING','PARTIAL','OVERDUE')")
    BigDecimal sumUpcoming(@Param("companyId") Long companyId, @Param("branchId") Long branchId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
}
