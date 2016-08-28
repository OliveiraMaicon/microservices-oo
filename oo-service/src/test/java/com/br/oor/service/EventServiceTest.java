package com.br.oor.service;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;

import com.br.oor.Application;
import com.br.oor.exception.AddressNotFoundException;
import com.br.oor.exception.AddressRequiredException;
import com.br.oor.model.Address;
import com.br.oor.model.Event;
import com.br.oor.repository.AddressRepository;
import com.br.oor.repository.EventRepository;

/**
 * Created by agomes on 17/01/16.
 */
@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EventServiceTest {

	@InjectMocks
	private EventService eventService;

	@Mock
	private EventRepository eventRepository;

	@Mock
	private AddressRepository addressRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = AddressNotFoundException.class)
	public void shouldThrowAExceptionForAddressNotFound() {
		Address address = new Address();
		address.setId(123L);
		Event event = new Event();
		event.setAddress(address);

		Mockito.when(addressRepository.findById(address.getId())).thenReturn(Optional.empty());

		eventService.save(event);
	}

	@Test(expected = AddressRequiredException.class)
	public void shouldThrowAExceptionForAddressRequired() {
		Event event = new Event();
		eventService.save(event);
	}

	@Test
	public void shouldSaveAEventWithAddress() {
		Address address = new Address();
		address.setId(123L);
		Event event = new Event();
		event.setAddress(address);

		Long id = address.getId();
		Mockito.when(addressRepository.findById(id)).thenReturn(Optional.of(address));

		eventService.save(event);

		Mockito.verify(addressRepository, Mockito.times(1)).findById(id);
	}

	@Test
	public void shouldSaveAEventAndCreateAddress() {
		Address address = new Address();
		Event event = new Event();
		event.setAddress(address);

		Long id = address.getId();
		Mockito.when(addressRepository.findById(id)).thenReturn(Optional.of(address));

		eventService.save(event);

		Mockito.verify(addressRepository, Mockito.never()).findById(id);
	}
}
