package com.example.helpdesk.backend.repository;

import com.example.helpdesk.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT COUNT(t) FROM Ticket t WHERE FUNCTION('DATE', t.creationDate) = CURRENT_DATE")
    int countByCreationDate();
}
