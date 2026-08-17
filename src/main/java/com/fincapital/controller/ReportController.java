package com.fincapital.controller;

import com.fincapital.dto.TransactionResponse;
import com.fincapital.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService s;

    public ReportController(ReportService s) {
        this.s = s;
    }

    @GetMapping("/overall")
    public List<TransactionResponse> overall(@RequestParam Long companyId, @RequestParam Long branchId, @RequestParam LocalDate from, @RequestParam LocalDate to) {
        return s.overall(companyId, branchId, from, to);
    }
}
