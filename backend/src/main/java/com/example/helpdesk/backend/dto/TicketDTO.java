package com.example.helpdesk.backend.dto;

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
    private int status;
    private String createdBy;
    private Date creationDate;
    private Date closureDate;
    private Long userid;
    private Long categoryId;
    //private List<ActivityDTO> activityLogs;
}
