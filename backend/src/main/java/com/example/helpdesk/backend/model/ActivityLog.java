package com.example.helpdesk.backend.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Data
@Table(name = "activity_log")
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date timestamp;
    private String description;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticketId;

    @ManyToOne
    @JoinColumn(name = "ticket_status", nullable = false)
    private Ticket ticketStatus;
}
