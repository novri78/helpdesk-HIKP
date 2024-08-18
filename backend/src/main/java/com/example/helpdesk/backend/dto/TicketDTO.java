package com.example.helpdesk.backend.dto;

import com.example.helpdesk.backend.constant.PriorityStatus;
import com.example.helpdesk.backend.constant.TicketStatus;
import com.example.helpdesk.backend.model.Category;
import com.example.helpdesk.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private Long id;
    private String ticketNo;
    private String title;
    private String description;
    private PriorityStatus priorityStatus;
    private TicketStatus ticketStatus;
    private String assignTo;
    private LocalDateTime createDate;
    private LocalDateTime closeDate;
    private User userId;
    private Category categoryId;
}
