package com.example.helpdesk.backend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ActivityDTO {
    private Long id;
    private Date timestamp;
    private String description;
    private Long ticketId;
}
