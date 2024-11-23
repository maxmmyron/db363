package com.mmyron.db363.repo;

import org.springframework.data.repository.CrudRepository;

import com.mmyron.db363.entitiy.Ticket;

public interface TicketRepo extends CrudRepository<Ticket, Long> {}
