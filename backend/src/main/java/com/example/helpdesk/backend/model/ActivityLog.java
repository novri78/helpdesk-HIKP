package com.example.helpdesk.backend.model;

import com.example.helpdesk.backend.util.TicketStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Getter
@Setter
@Table(name = "activity_log")
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("timestamp")
    private Date timestamp;

    @JsonProperty("description")
    private String description;

    @JsonProperty("tickets_status")
    @JoinColumn(name = "tickets_status", nullable = false)
    private TicketStatus ticketStatus;

    @ManyToOne
    @JsonProperty("tickets_id")
    @JoinColumn(name = "tickets_id", nullable = false)
    private Ticket ticketId;



}
