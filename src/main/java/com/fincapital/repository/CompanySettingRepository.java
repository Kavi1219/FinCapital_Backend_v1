package com.fincapital.repository;

import com.fincapital.entity.CompanySetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanySettingRepository extends JpaRepository<CompanySetting, Long> {
}
