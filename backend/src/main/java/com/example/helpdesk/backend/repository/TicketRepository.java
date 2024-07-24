package com.example.helpdesk.backend.repository;

import com.example.helpdesk.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
