package com.fincapital.controller;

import com.fincapital.dto.*;
import com.fincapital.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService s;

    public CompanyController(CompanyService s) {
        this.s = s;
    }

    @GetMapping
    public List<CompanyResponse> all() {
        return s.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse create(@Valid @RequestBody CreateCompanyRequest r) {
        return s.create(r);
    }
}
