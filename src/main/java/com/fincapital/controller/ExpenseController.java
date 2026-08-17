package com.fincapital.controller;

import com.fincapital.dto.*;
import com.fincapital.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService s;

    public ExpenseController(ExpenseService s) {
        this.s = s;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@Valid @RequestBody CreateExpenseRequest r) {
        return s.create(r);
    }

    @GetMapping
    public List<ExpenseResponse> all(@RequestParam Long companyId, @RequestParam Long branchId) {
        return s.all(companyId, branchId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        s.delete(id);
    }
}
