package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.ActivityDTO;
import com.example.helpdesk.backend.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
public class ActivityLogController {
    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getAllActivity() {
        List<ActivityDTO> activityDTOList = activityLogService.getActivityLogs ();
        return ResponseEntity.ok ( activityDTOList );
    }

    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<List<ActivityDTO>> getActivityLogsByTicketId(@PathVariable Long ticketId) {
        List<ActivityDTO> activityLogs = activityLogService.getActivityLogsByTicketId(ticketId);
        return ResponseEntity.ok(activityLogs);
    }
}
