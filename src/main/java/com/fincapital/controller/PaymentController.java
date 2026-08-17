package com.fincapital.controller;

import com.fincapital.dto.*;
import com.fincapital.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService s;

    public PaymentController(PaymentService s) {
        this.s = s;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody CreatePaymentRequest r) {
        return s.create(r);
    }

    @GetMapping
    public List<PaymentResponse> all(@RequestParam Long companyId, @RequestParam Long branchId) {
        return s.all(companyId, branchId);
    }
}
