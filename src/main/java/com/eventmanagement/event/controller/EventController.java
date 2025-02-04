/**
 * 
 */
package com.eventmanagement.event.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.eventmanagement.event.entity.Event;
import com.eventmanagement.event.service.EventService;

/**
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/api/eventmanagement/event")
public class EventController {

	@Autowired
	private EventService eventService;

	@GetMapping("healthcheck")
	public ResponseEntity<String> healthcheck() {
		return new ResponseEntity<>("Event Service is Running", HttpStatus.OK);
	}

	@GetMapping("getEvent/{eventId}")
	public ResponseEntity<Event> getEventDetails(@PathVariable(value = "eventId", required = true) String eventId) {
		Event event = null;
		if (null != eventId && !eventId.isBlank()) {
			event = eventService.getEventById(eventId);
		} else {
			return new ResponseEntity<Event>(new Event(), HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(event);
	}

	@GetMapping("getEvent/location/{eventLocation}")
	public ResponseEntity<List<Event>> getEventDetailsByLocation(
			@PathVariable(value = "eventLocation", required = false) String eventLocation) {
		List<Event> eventList = new ArrayList<Event>();
		if (null != eventLocation && !eventLocation.isBlank()) {
			eventList = eventService.getEventDetailsByLocation(eventLocation.toUpperCase());
		} else {
			eventList = eventService.getAllEvents();
		}
		return ResponseEntity.ok(eventList);
	}

	@GetMapping("getAllEvents")
	public ResponseEntity<List<Event>> getAllEvents() {
		return ResponseEntity.ok(eventService.getAllEvents());
	}

	@PostMapping("addEvent")
	public ResponseEntity<String> addEvent(@Valid @RequestBody Event event) {
		if (null == isAdmin(event.getUserName())) {
			return new ResponseEntity<String>("User Does Not Exists - Only Admin Users Can Add/Update Events",
					HttpStatus.BAD_REQUEST);
		}
		if (null != event.getUserName() && isAdmin(event.getUserName())) {
			Event newEvent = null;
			newEvent = eventService.createEvent(event);
			if (null != newEvent) {
				return ResponseEntity.ok("Event Added Successfully !!!!");
			} else {
				return new ResponseEntity<String>("EventId Already Exists", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Insufficient Privilege - Only Admin Users Can Add/Update Events",
					HttpStatus.FORBIDDEN);
		}

	}

	@PostMapping("updateEvent")
	public ResponseEntity<String> updateEvent(@Valid @RequestBody Event event) {
		if (null == isAdmin(event.getUserName())) {
			return new ResponseEntity<String>("User Does Not Exists - Only Admin Users Can Add/Update Events",
					HttpStatus.BAD_REQUEST);
		}
		if (null != event.getUserName() && isAdmin(event.getUserName())) {
			Event newEvent = null;
			newEvent = eventService.updateEvent(event);
			if (null != newEvent) {
				return ResponseEntity.ok("Event Updated Successfully !!!!");
			} else {
				return new ResponseEntity<String>("Event Does Not Exists - Check Data", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>("Insufficient Privilege - Only Admin Users Can Add/Update Events",
					HttpStatus.FORBIDDEN);
		}
	}

	private Boolean isAdmin(String userName) {
		ResponseEntity<Boolean> isAdmin = new RestTemplate().getForEntity(
				"http://2044739userServiceLB-1566141824.us-west-2.elb.amazonaws.com/api/eventmanagement/user/isAdmin/{userName}", Boolean.class, userName);
		return isAdmin.getBody();

	}

	@GetMapping("isEventIdValid/{eventId}")
	public ResponseEntity<Boolean> isEventIdValid(@PathVariable(value = "eventId", required = true) String eventId) {
		if (null != eventId && !eventId.isBlank()) {
			Event event = eventService.getEventById(eventId);
			if (null != event && null != event.getEventId()) {
				return ResponseEntity.ok(true);
			}
		}
		return ResponseEntity.ok(false);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationException(MethodArgumentNotValidException mex) {

		Map<String, String> validationError = new HashMap<String, String>();

		mex.getBindingResult().getAllErrors().forEach(error -> {
			validationError.put(error.getObjectName(), error.getDefaultMessage());
		});

		return validationError;

	}

}
