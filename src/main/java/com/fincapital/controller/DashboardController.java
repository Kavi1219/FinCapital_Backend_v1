package com.fincapital.controller;

import com.fincapital.dto.DashboardSummary;
import com.fincapital.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService s;

    public DashboardController(DashboardService s) {
        this.s = s;
    }

    @GetMapping("/summary")
    public DashboardSummary summary(@RequestParam Long companyId, @RequestParam Long branchId) {
        return s.summary(companyId, branchId);
    }
}
