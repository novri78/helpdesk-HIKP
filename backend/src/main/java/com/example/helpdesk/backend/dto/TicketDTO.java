package com.example.helpdesk.backend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TicketDTO {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private Date creationDate;
    private Date closureDate;
    private Long userId;
    private Long categoryId;
}
