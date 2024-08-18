package com.example.helpdesk.backend.model;

import com.example.helpdesk.backend.constant.PriorityStatus;
import com.example.helpdesk.backend.constant.TicketStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(unique = true, nullable = false)
    @JsonProperty("ticketNo")
    private String ticketNo;

    @Column(nullable = false)
    @Size(min = 1, max = 255)
    @JsonProperty("title")
    private String title;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("description")
    private String description;

    @Enumerated(EnumType.STRING)
    @JsonProperty("priority")
    private PriorityStatus priorityStatus;

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private TicketStatus ticketStatus;

    @JsonProperty("assignTo")
    private String assignTo;

    @CreationTimestamp
    @JsonProperty("createDate")
    private LocalDateTime createDate;

    @CreationTimestamp
    @JsonProperty("closeDate")
    private LocalDateTime closeDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


}
