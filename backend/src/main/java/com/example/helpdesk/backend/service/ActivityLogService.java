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

    private ActivityDTO convertToDTO(ActivityLog activityLog) {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(activityLog.getId());
        activityDTO.setTimestamp(activityLog.getTimestamp());
        activityDTO.setDescription(activityLog.getDescription());
        activityDTO.setTicketStatus (activityLog.getTicketStatus ().getStatus ());
        activityDTO.setTicketId(activityLog.getTicketId ( ).getId ());
        return activityDTO;
    }

    private ActivityLog convertToEntity(ActivityDTO activityDTO) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setTimestamp(activityDTO.getTimestamp());
        activityLog.setDescription(activityDTO.getDescription());
        return activityLog;
    }

    public List<ActivityDTO> getAllActivityLogs() {
        return activityLogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ActivityDTO getActivityLogById(Long id) {
        ActivityLog activityLog = activityLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity log not found"));
        return convertToDTO(activityLog);
    }

//    public ActivityDTO createActivityLog(ActivityDTO activityDTO) {
//        ActivityLog activityLog = convertToEntity(activityDTO);
//        activityLog = activityLogRepository.save(activityLog);
//        return convertToDTO(activityLog);
//    }

    public void deleteActivityLogById(Long id) {
        activityLogRepository.deleteById(id);
    }
}
