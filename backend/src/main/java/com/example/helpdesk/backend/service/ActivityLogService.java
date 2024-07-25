package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.model.ActivityLog;
import com.example.helpdesk.backend.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityLogService {
    @Autowired
    private ActivityLogRepository activityLogRepository;

    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAll ();
    }

    public ActivityLog getActivityLogById(Long id) {
        Optional<ActivityLog> activityLog = activityLogRepository.findById (id);
        if(activityLog.isPresent ()) {
            return activityLog.get ();
        } else {
            throw new RuntimeException("User not found for id :: " + id);
        }
    }

    public ActivityLog createActivityLog(ActivityLog activityLog) {
        return activityLogRepository.save (activityLog);
    }

    public void deleteActivityLogById(Long id) {
        activityLogRepository.deleteById (id);
    }
}
