package com.fincapital.repository;

import com.fincapital.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    // All documents belonging to a customer
    List<Document> findByCustomer_IdOrderByUploadedAtDesc(Long customerId);

    // All documents belonging to a Jamin
    List<Document> findByJamin_IdOrderByUploadedAtDesc(Long jaminId);
}