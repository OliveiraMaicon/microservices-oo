package com.br.oor.controller;

import javax.validation.Valid;

import com.br.oor.enuns.State;
import com.br.oor.model.form.EventSearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.br.oor.exception.AddressAlreadyExistException;
import com.br.oor.exception.AddressNotFoundException;
import com.br.oor.exception.AddressRequiredException;
import com.br.oor.exception.EventInvalidForUpdade;
import com.br.oor.exception.EventNotFoundException;
import com.br.oor.exception.ValidationException;
import com.br.oor.model.Event;
import com.br.oor.service.EventService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by agomes on 15/01/16.
 */
@RestController
@RequestMapping("events")
public class EventsController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(EventsController.class);

	@Autowired
	private EventService eventService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Event> create(@RequestBody @Valid Event event, BindingResult result) throws Exception {
		try {
			Event created = eventService.save(event);
			LOGGER.info(String.format("create(%s)", created.toString()));
			return new ResponseEntity<Event>(created, HttpStatus.CREATED);
		} catch (AddressRequiredException are) {
			LOGGER.error(are.getMessage());
			throw new ValidationException(HttpStatus.BAD_REQUEST, are.getMessage(), "address");
		} catch (AddressNotFoundException anfe) {
			LOGGER.error(anfe.getMessage());
			throw new ValidationException(HttpStatus.NOT_FOUND, anfe.getMessage(), event.getAddress().getId()
					.toString());
		} catch (AddressAlreadyExistException aaee) {
			LOGGER.error(aaee.getMessage());
			throw new ValidationException(HttpStatus.CONFLICT, aaee.getMessage(), "lagitude_and_longitude");
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Event> update(@RequestBody @Valid Event event, BindingResult result) throws Exception {

		try {
			Event created = eventService.update(event);
			LOGGER.info(String.format("update(%s)", created.toString()));
			return new ResponseEntity<Event>(created, HttpStatus.OK);
		} catch (EventInvalidForUpdade e) {
			LOGGER.error(e.getMessage());
			throw new ValidationException(HttpStatus.BAD_REQUEST, e.getMessage(), "id");
		}catch(EventNotFoundException enfe){
			LOGGER.error(enfe.getMessage());
			throw new ValidationException(HttpStatus.NOT_FOUND, enfe.getMessage(), event.getId().toString());
		} catch (AddressNotFoundException anfe) {
			LOGGER.error(anfe.getMessage());
			throw new ValidationException(HttpStatus.NOT_FOUND, anfe.getMessage(), event.getAddress().getId()
					.toString());
		} catch (AddressAlreadyExistException aaee) {
			LOGGER.error(aaee.getMessage());
			throw new ValidationException(HttpStatus.CONFLICT, aaee.getMessage(), "lagitude_and_longitude");
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Event>> findAllByPage(Pageable p) throws Exception {
		try {
			Page<Event> events = eventService.findByPage(p);
			return new ResponseEntity<Page<Event>>(events, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(String.format("findAllByPage(%s)", p.toString()));
			LOGGER.error(e.getMessage());
			throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@RequestMapping(path = "{id}", method = RequestMethod.GET)
	public ResponseEntity<Event> findbyId(@PathVariable Long id) throws Exception {

		try {
			Event event = eventService.findById(id);
			return new ResponseEntity<Event>(event, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(String.format("findbyId(%s)", id));
			LOGGER.error(e.getMessage());
			throw new ValidationException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@RequestMapping(path = "{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Event> delete(@PathVariable Long id) throws Exception {

		try {
			eventService.delete(id);
			LOGGER.info(String.format("delete(%s)",id));
			return new ResponseEntity<Event>(HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(String.format("delete(%s)", id));
			LOGGER.error(e.getMessage());
			throw new ValidationException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@RequestMapping(path = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> search(@RequestParam("fieldSearch") String fieldSearch,
                                    @RequestParam(value = "date", required = true) LocalDateTime date) throws Exception {
		try{
			EventSearchForm form = new EventSearchForm(fieldSearch,date);
			return new ResponseEntity<List<EventSearchForm>>(eventService.search(form),HttpStatus.GONE);

		}catch (Exception e){
			LOGGER.error(String.format("Search(%s,%d)", fieldSearch , date));
			LOGGER.error(e.getMessage());
			throw new ValidationException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@RequestMapping(path = "/cities", method = RequestMethod.GET)
	public ResponseEntity<?> getCities(){
		return new ResponseEntity<Map<String, String>>(State.getValuesMap(),HttpStatus.OK);
	}

}
