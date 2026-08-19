package com.fincapital.controller;

import com.fincapital.dto.*;
import com.fincapital.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService s;

    public CustomerController(
            CustomerService s
    ) {
        this.s = s;
    }

    // =========================================================
    // CREATE CUSTOMER
    // =========================================================

    @PostMapping
    @ResponseStatus(
            HttpStatus.CREATED
    )
    public CustomerResponse create(
            @Valid
            @RequestBody
            CreateCustomerRequest r
    ) {

        return s.create(
                r
        );
    }

    // =========================================================
    // GET ALL CUSTOMERS
    // =========================================================

    @GetMapping
    public List<CustomerResponse> all(

            @RequestParam
            Long companyId,

            @RequestParam
            Long branchId
    ) {

        return s.all(
                companyId,
                branchId
        );
    }

    // =========================================================
    // CUSTOMER PROFILE
    //
    // IMPORTANT:
    // Must appear before /{id} logically for readability.
    // Spring can distinguish both routes.
    // =========================================================

    @GetMapping("/{id}/profile")
    public CustomerProfileResponse profile(
            @PathVariable
            Long id
    ) {

        return s.profile(
                id
        );
    }

    // =========================================================
    // GET ONE CUSTOMER
    // =========================================================

    @GetMapping("/{id}")
    public CustomerResponse one(
            @PathVariable
            Long id
    ) {

        return s.one(
                id
        );
    }
}