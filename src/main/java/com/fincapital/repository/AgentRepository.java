package com.fincapital.repository;

import com.fincapital.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findByCompany_IdOrderByIdDesc(Long companyId);

    long countByCompany_IdAndEmployeeCodeIsNotNull(Long companyId);
}
