/**
 * 
 */
package com.eventmanagement.event.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.eventmanagement.event.entity.Event;

/**
 * @author Administrator
 *
 */
@Service
public interface EventService {

	public List<Event> getAllEvents();

	public Event getEventById(String eventId);

	public List<Event> getEventDetailsByLocation(String eventLocation);

	public Event createEvent(@Valid Event event);

	public Event updateEvent(@Valid Event event);

}
