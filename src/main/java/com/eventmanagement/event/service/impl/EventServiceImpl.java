/**
 * 
 */
package com.eventmanagement.event.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eventmanagement.event.entity.Event;
import com.eventmanagement.event.repository.EventRepository;
import com.eventmanagement.event.service.EventService;

/**
 * @author Administrator
 *
 */
@Component
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepo;

	@Override
	public List<Event> getAllEvents() {
		return eventRepo.findAll();
	}

	@Override
	public Event getEventById(String eventId) {
		if (isEventExistsById(eventId)) {
			Optional<Event> event = eventRepo.findById(eventId);
			return event.get();
		} else {
			return null;
		}
	}

	@Override
	public List<Event> getEventDetailsByLocation(String eventLocation) {
		return eventRepo.findByLocation(eventLocation);
	}

	@Override
	public Event createEvent(@Valid Event event) {
		if (isEventExistsById(event.getEventId())) {
			return null;
		} else {
			event.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			event.setLocation(event.getLocation().toUpperCase());
			eventRepo.save(event);
		}
		return event;
	}

	@Override
	public Event updateEvent(@Valid Event event) {
		if (isEventExistsById(event.getEventId())) {
			event.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			event.setLocation(event.getLocation().toUpperCase());
			eventRepo.save(event);
		} else {
			return null;
		}
		return event;
	}

	private boolean isEventExistsById(String eventId) {
		return eventRepo.existsById(eventId);
	}

}
