package com.fincapital.controller;

import com.fincapital.dto.*;
import com.fincapital.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
public class AgentController {
    private final AgentService s;

    public AgentController(AgentService s) {
        this.s = s;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AgentResponse register(@Valid @RequestBody RegisterAgentRequest r) {
        return s.register(r);
    }

    @PostMapping("/{id}/verify-mobile")
    public AgentResponse verify(@PathVariable Long id) {
        return s.verify(id);
    }

    @PostMapping("/{id}/approve")
    public AgentResponse approve(@PathVariable Long id, @RequestBody(required = false) AgentDecisionRequest r) {
        return s.approve(id, r == null ? new AgentDecisionRequest(null, null) : r);
    }

    @PostMapping("/{id}/reject")
    public AgentResponse reject(@PathVariable Long id, @RequestBody(required = false) AgentDecisionRequest r) {
        return s.reject(id, r == null ? new AgentDecisionRequest(null, null) : r);
    }

    @GetMapping
    public List<AgentResponse> all(@RequestParam Long companyId) {
        return s.byCompany(companyId);
    }
}
