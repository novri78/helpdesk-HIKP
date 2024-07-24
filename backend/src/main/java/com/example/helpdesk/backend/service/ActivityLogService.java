package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.model.ActivityLog;
import com.example.helpdesk.backend.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogService {
    @Autowired
    private ActivityLogRepository activityLogRepository;

    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAll ();
    }

    public ActivityLog createActivityLog(ActivityLog activityLog) {
        return activityLogRepository.save (activityLog);
    }
}
