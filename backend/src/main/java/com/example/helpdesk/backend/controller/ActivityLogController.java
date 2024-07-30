package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.ActivityDTO;
import com.example.helpdesk.backend.model.ActivityLog;
import com.example.helpdesk.backend.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
public class ActivityLogController {
    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping
    public List<ActivityDTO> getAllActivityLogs() {
        return activityLogService.getAllActivityLogs ();
    }

    @GetMapping("/{id}")
    public ActivityDTO getActivityLogById(@PathVariable Long id) {
        return activityLogService.getActivityLogById (id);
    }

    @PostMapping
    public ActivityDTO createActivityLog(@RequestBody ActivityDTO activityDTO) {
        return activityLogService.createActivityLog (activityDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteActivityLogById(@PathVariable Long id) {
        activityLogService.deleteActivityLogById (id);
    }
}
