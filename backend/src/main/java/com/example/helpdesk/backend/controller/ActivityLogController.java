package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.model.ActivityLog;
import com.example.helpdesk.backend.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
public class ActivityLogController {
    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping
    public List<ActivityLog> getAllActivityLogs() {
        return activityLogService.getAllActivityLogs ();
    }

    @GetMapping("/{id}")
    public ActivityLog getActivityLogById(@PathVariable Long id) {
        return  activityLogService.getActivityLogById (id);
    }

    @PostMapping
    public ActivityLog createActivityLog(@RequestBody ActivityLog activityLog) {
        return activityLogService.createActivityLog (activityLog);
    }

    @DeleteMapping("/{id}")
    public void deleteActivityLogById(@PathVariable Long id) {
        activityLogService.deleteActivityLogById (id);
    }
}
