package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.ActivityDTO;
import com.example.helpdesk.backend.model.ActivityLog;
import com.example.helpdesk.backend.repository.ActivityLogRepository;
import com.example.helpdesk.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityLogService {
    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private ActivityDTO mapToDTO(ActivityLog activityLog) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(activityLog.getId());
        activityDTO.setTimestamp(activityLog.getTimestamp());
        activityDTO.setDescription(activityLog.getDescription());
        activityDTO.setTicketStatus(activityLog.getTicketStatus ());
        activityDTO.setTicketId (activityLog.getTicket ().getId ());
        return activityDTO;
    }

    public List<ActivityDTO> getActivityLogs() {
        List<ActivityLog> activityLogs = activityLogRepository.findAll ();
        return activityLogs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ActivityDTO> getActivityLogsByTicketId(Long ticketId) {
        List<ActivityLog> activityLogs = activityLogRepository.findByTicketId(ticketId);
        return activityLogs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }


}
