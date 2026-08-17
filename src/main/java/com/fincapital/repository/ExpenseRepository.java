package com.fincapital.repository;

import com.fincapital.entity.Expense;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCompany_IdAndBranch_IdOrderByExpenseDateDescIdDesc(Long companyId, Long branchId);

    long countByCompany_Id(Long companyId);

    @Query("select coalesce(sum(e.amount),0) from Expense e where e.company.id=:companyId and e.branch.id=:branchId and e.expenseDate=:date")
    BigDecimal sumForDate(@Param("companyId") Long companyId, @Param("branchId") Long branchId, @Param("date") LocalDate date);
}
