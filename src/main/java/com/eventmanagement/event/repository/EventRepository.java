/**
 * 
 */
package com.eventmanagement.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventmanagement.event.entity.Event;

/**
 * @author Administrator
 *
 */
public interface EventRepository extends JpaRepository<Event, String> {

	public List<Event> findByLocation(String location);

}
