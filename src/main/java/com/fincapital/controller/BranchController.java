package com.fincapital.controller;

import com.fincapital.dto.*;
import com.fincapital.service.BranchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {
    private final BranchService s;

    public BranchController(BranchService s) {
        this.s = s;
    }

    @GetMapping
    public List<BranchResponse> all(@RequestParam Long companyId) {
        return s.byCompany(companyId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BranchResponse create(@Valid @RequestBody CreateBranchRequest r) {
        return s.create(r);
    }
}
