package com.example.helpdesk.backend.repository;

import com.example.helpdesk.backend.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}
