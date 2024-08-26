package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.constant.Role;
import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.mapper.TicketMapper;
import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.CategoryRepository;
import com.example.helpdesk.backend.repository.TicketRepository;
import com.example.helpdesk.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private final TicketRepository ticketRepository;

    @Autowired
    private final TicketMapper ticketMapper;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    private String generateUniqueTicketNo() {
        return "TCKT-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Method to validate if the assigned user has the SUPPORT role
    private void validateSupportUser(Long assignTo) {
        Optional<User> supportUser = userRepository.findByIdAndRole (assignTo, Role.SUPPORT);

        if (supportUser.isEmpty()) {
            throw new IllegalArgumentException("The user assigned does not have the SUPPORT role.");
        }
    }

    @Transactional
    public TicketDTO createNewTicket(@Valid TicketDTO ticketDTO) {
        logger.info("Creating new ticket with userId: {}", ticketDTO.getUserId ());
        validateSupportUser(ticketDTO.getAssignTo ());
        ticketDTO.setId (ticketDTO.getId ( ));
        ticketDTO.setTicketNo(generateUniqueTicketNo());
        ticketDTO.setTicketStatus (ticketDTO.getTicketStatus ());
        ticketDTO.setPriorityStatus (ticketDTO.getPriorityStatus ());
        ticketDTO.setTitle (ticketDTO.getTitle ());
        ticketDTO.setDescription (ticketDTO.getDescription ());
        ticketDTO.setCreateDate (ticketDTO.getCreateDate ());
        ticketDTO.setCloseDate (ticketDTO.getCloseDate ());
        ticketDTO.setUserId (ticketDTO.getUserId ( ));
        ticketDTO.setCategoryId (ticketDTO.getCategoryId ());

        Ticket ticket = ticketMapper.toEntity(ticketDTO);

        ticketRepository.save(ticket);
        return ticketMapper.toDTO(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketDTO> getAllTickets() {
        logger.info("Fetching all tickets");
        return  ticketRepository.findAll ().stream ()
                .map (ticketMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TicketDTO getTicketById(Long id) {
        logger.info("Fetching user with id: {}", id);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found for id: " + id));
        return ticketMapper.toDTO (ticket);
    }

    @Transactional
    public TicketDTO updateTicketById(Long id, @Valid TicketDTO ticketDTO) {
        logger.info("Updating user with id: {}", id);
        Ticket existingTicket = ticketRepository.findById (id)
                .orElseThrow(() -> new RuntimeException("Ticket not found for id :: " + id));
        ticketMapper.updateEntity (ticketDTO, existingTicket );
        Ticket updateTicket = ticketRepository.save (existingTicket);
        logger.info("Ticket updated successfully: {}", updateTicket);
        return ticketMapper.toDTO (updateTicket);
    }

}