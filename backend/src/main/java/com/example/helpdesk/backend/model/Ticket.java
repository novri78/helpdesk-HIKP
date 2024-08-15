package com.example.helpdesk.backend.model;

import com.example.helpdesk.backend.constant.PriorityStatus;
import com.example.helpdesk.backend.constant.TicketStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @JsonProperty("ticketNo")
    private String ticketNo;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("priority")
    private PriorityStatus priorityStatus;

    @JsonProperty("status")
    private TicketStatus ticketStatus;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("creationDate")
    private Date creationDate;

    @JsonProperty("closureDate")
    private Date closureDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "ticketId")
    private List<ActivityLog> activityLogs;


}
