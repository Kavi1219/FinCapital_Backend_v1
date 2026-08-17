package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgentService {
    private final AgentRepository agents;
    private final CompanyRepository companies;
    private final BranchRepository branches;
    private final AppUserRepository users;
    private final NotificationRepository notifications;
    private final PasswordEncoder encoder;
    private final CodeService codes;

    public AgentService(AgentRepository a, CompanyRepository b, BranchRepository c, AppUserRepository d, NotificationRepository e, PasswordEncoder f, CodeService g) {
        agents = a;
        companies = b;
        branches = c;
        users = d;
        notifications = e;
        encoder = f;
        codes = g;
    }

    @Transactional
    public AgentResponse register(RegisterAgentRequest r) {
        Company c = companies.findById(r.companyId()).orElseThrow(() -> new NotFoundException("Company not found"));
        Branch b = branches.findById(r.branchId()).orElseThrow(() -> new NotFoundException("Branch not found"));
        Agent a = new Agent();
        a.setCompany(c);
        a.setBranch(b);
        a.setAgentName(r.agentName());
        a.setMobile(r.mobile());
        a.setPasswordHash(encoder.encode(r.password()));
        a.setProfilePhotoUrl(r.profilePhotoUrl());
        a.setApprovalStatus("PENDING_OTP");
        return map(agents.save(a));
    }

    @Transactional
    public AgentResponse verify(Long id) {
        Agent a = get(id);
        a.setMobileVerified(true);
        a.setApprovalStatus("PENDING_APPROVAL");
        return map(agents.save(a));
    }

    @Transactional
    public AgentResponse approve(Long id, AgentDecisionRequest r) {
        Agent a = get(id);
        if (!Boolean.TRUE.equals(a.getMobileVerified()))
            throw new IllegalArgumentException("Agent mobile must be OTP verified before approval");
        AppUser u = r.approvedByUserId() == null ? null : users.findById(r.approvedByUserId()).orElseThrow(() -> new NotFoundException("Approver not found"));
        a.setEmployeeCode(codes.employeeCode(a.getCompany().getCompanyCode(), agents.countByCompany_IdAndEmployeeCodeIsNotNull(a.getCompany().getId()) + 1));
        a.setApprovalStatus("ACTIVE");
        a.setApprovedBy(u);
        a.setApprovedAt(LocalDateTime.now());
        a.setRejectionReason(null);
        a = agents.save(a);
        Notification n = new Notification();
        n.setCompany(a.getCompany());
        n.setAgent(a);
        n.setTitle("Agent Registration Approved");
        n.setMessage("Registration successful. Employee ID: " + a.getEmployeeCode());
        n.setNotificationType("AGENT_APPROVED");
        notifications.save(n);
        return map(a);
    }

    @Transactional
    public AgentResponse reject(Long id, AgentDecisionRequest r) {
        Agent a = get(id);
        a.setApprovalStatus("REJECTED");
        a.setRejectionReason(r.rejectionReason());
        return map(agents.save(a));
    }

    public List<AgentResponse> byCompany(Long id) {
        return agents.findByCompany_IdOrderByIdDesc(id).stream().map(this::map).toList();
    }

    private Agent get(Long id) {
        return agents.findById(id).orElseThrow(() -> new NotFoundException("Agent not found"));
    }

    private AgentResponse map(Agent a) {
        return new AgentResponse(a.getId(), a.getCompany().getId(), a.getBranch().getId(), a.getEmployeeCode(), a.getAgentName(), a.getMobile(), a.getProfilePhotoUrl(), a.getMobileVerified(), a.getApprovalStatus(), a.getRejectionReason());
    }
}
