package com.fincapital.controller;

import com.fincapital.dto.*;
import com.fincapital.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService s;

    public LoanController(LoanService s) {
        this.s = s;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse create(@Valid @RequestBody CreateLoanRequest r) {
        return s.create(r);
    }

    @GetMapping
    public List<LoanResponse> all(@RequestParam Long companyId, @RequestParam Long branchId) {
        return s.all(companyId, branchId);
    }

    @GetMapping("/{id}")
    public LoanResponse one(@PathVariable Long id) {
        return s.one(id);
    }
}
