package com.br.oor.service;


import com.br.oor.model.form.EventSearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.br.oor.exception.AddressAlreadyExistException;
import com.br.oor.exception.AddressNotFoundException;
import com.br.oor.exception.AddressRequiredException;
import com.br.oor.exception.EventInvalidForUpdade;
import com.br.oor.exception.EventNotFoundException;
import com.br.oor.model.Address;
import com.br.oor.model.Event;
import com.br.oor.repository.AddressRepository;
import com.br.oor.repository.EventRepository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by agomes on 15/01/16.
 */
@Service
public class EventService {

	private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
	
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private AddressRepository addressRepository;

	@Transactional(propagation = Propagation.REQUIRED)
	public Event save(Event event) {
		LOGGER.debug(String.format("save(%s)", event.toString()));
		if (event.getAddress() == null) {
			throw new AddressRequiredException("Endereço obrigatório para um evento.");
		} else {
			if (event.getAddress().getId() != null) {
				Address address = addressRepository
						.findById(event.getAddress().getId())
						.orElseThrow(
								() -> new AddressNotFoundException("Endereço não encontrado."));
					event.setAddress(address);
				}
		}
		try{
			return eventRepository.save(event);
		}catch (DataIntegrityViolationException dve){
			LOGGER.error(dve.getMessage());
			throw new AddressAlreadyExistException("Não é possivel criar um endereço com Latitude e Longitude ja cadastradas.");
		}
	}

	@Transactional(readOnly=false)
	public Event update(final Event event) {
		LOGGER.debug(String.format("update(%s)", event.toString()));
		if(event.getId() == null){
			throw new EventInvalidForUpdade("Informe o ID do Evento para Update.");
		}
		//Verify if exist
		findById(event.getId());
		return save(event);
	}
	
	public void delete(Long id){
		LOGGER.debug(String.format("delete(%s)", id));
		eventRepository.delete(id);
	}
	
	@Transactional(readOnly = true)
	public Event findById(Long id){
		LOGGER.debug(String.format("findById(%s)", id));
		return eventRepository.findById(id)
				.orElseThrow(
					() -> new EventNotFoundException("Evento não encontrado"));
	}
	
	@Transactional(readOnly = true)
	public Page<Event> findByPage(Pageable page) {
		LOGGER.debug(String.format("findByPage(%s)", page.toString()));
		return eventRepository.findAll(page);
	}

	@Transactional(readOnly = true)
	public List<EventSearchForm> search(EventSearchForm form){
		List<EventSearchForm> returnList =  new ArrayList<EventSearchForm>();
		if(!StringUtils.isEmpty(form.getFieldSearch())){
			returnList = eventRepository.search(form.getFieldSearch(),form.getDate());
		}else{
			returnList = eventRepository.findByEventDate(form.getDate());
		}

		return returnList;

	}




}
