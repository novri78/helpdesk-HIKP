package com.example.helpdesk.backend.repository;

import com.example.helpdesk.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT MAX(t.id) FROM Ticket t")
    Long findMaxId();

    @Query
            ("SELECT COUNT(t) FROM Ticket t WHERE DATE(t.creationDate) = DATE(:today)")
    long countByCreationDate(@Param ("today") String today);
}
