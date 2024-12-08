package com.mmyron.db363.repo;

import org.springframework.data.repository.CrudRepository;

import com.mmyron.db363.entitiy.Ticket;
import com.mmyron.db363.entitiy.TicketPK;

public interface TicketRepo extends CrudRepository<Ticket, TicketPK> {}
