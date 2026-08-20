package com.fincapital.controller;

import com.fincapital.dto.ReportSummaryResponse;
import com.fincapital.dto.TransactionResponse;
import com.fincapital.service.ReportService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService s;

    public ReportController(
            ReportService s
    ) {
        this.s = s;
    }

    // =========================================================
    // TRANSACTION LIST
    // =========================================================

    @GetMapping("/overall")
    public List<TransactionResponse> overall(

            @RequestParam Long companyId,

            @RequestParam Long branchId,

            @RequestParam LocalDate from,

            @RequestParam LocalDate to
    ) {

        return s.overall(
                companyId,
                branchId,
                from,
                to
        );
    }

    // =========================================================
    // REPORT SUMMARY
    // =========================================================

    @GetMapping("/summary")
    public ReportSummaryResponse summary(

            @RequestParam Long companyId,

            @RequestParam Long branchId,

            @RequestParam LocalDate from,

            @RequestParam LocalDate to
    ) {

        return s.summary(
                companyId,
                branchId,
                from,
                to
        );
    }
}