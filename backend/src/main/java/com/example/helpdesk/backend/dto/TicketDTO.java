package com.example.helpdesk.backend.dto;

import com.example.helpdesk.backend.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private int ticketStatus;
    private String createdBy;
    private Date creationDate;
    private Date closureDate;
    private Long userId;
    private Long categoryId;
    //private List<ActivityDTO> activityLogs;
//    public TicketStatus getTicketStatus() {
//        return ticketStatus;
//    }
//
//    public void setTicketStatus(TicketStatus ticketStatus) {
//        this.ticketStatus = ticketStatus;
//    }
}
