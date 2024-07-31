package com.example.helpdesk.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private Long id;

    @JsonProperty("timestamp")
    private Date timestamp;

    @JsonProperty("description")
    private String description;

    @JsonProperty("tickets_status")
    @JoinColumn(name = "tickets_status", nullable = false)
    private int ticketStatus;

    @ManyToOne
    @JsonProperty("tickets_id")
    @JoinColumn(name = "tickets_id", nullable = false)
    private Ticket ticket;



}
