package com.br.oor.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import com.br.oor.enuns.State;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.br.oor.Application;
import com.br.oor.model.Address;
import com.br.oor.model.Event;
import com.br.oor.repository.AddressRepository;
import com.br.oor.service.EventService;
import com.br.oor.utils.IntegrationTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@IntegrationTest("server.port:0")
@WebAppConfiguration
public class EventControllerTest {

	private static final String EVENTS = "/events";

	@Autowired
	private EmbeddedWebApplicationContext webApplicationContext;

	@Autowired
	private ObjectMapper mapper;

	private MockMvc mockMvc;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private EventService service;

	@Autowired
	private IntegrationTestUtils utils;

	private Address address;

	private LocalDateTime now;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();


		address = new Address();
		address.setName("Address");
		address.setLatitude(1L);
		address.setLongitude(1L);
		address.setCity(State.SAOPAULO.getNome());
		address.setUf(State.SAOPAULO.getSigla());


		now = LocalDateTime.of(2016, 01, 20, 0, 0);
	}

	@Test
	public void shouldCreateANewEvent() throws Exception {
		Event event = new Event();
		event.setName("Event");
		event.setDate(now);
		event.setAddress(address);

		String data = mapper.writeValueAsString(event);
		mockMvc.perform(post(EVENTS).contentType(MediaType.APPLICATION_JSON).content(data)).andExpect(
				status().isCreated());
	}

	@Test
	public void shouldNotCreateANewEventWithoutAddress() throws Exception {
		Event event = new Event();
		event.setName("Event");
		event.setDate(now);

		String data = mapper.writeValueAsString(event);
		mockMvc.perform(post(EVENTS).contentType(MediaType.APPLICATION_JSON).content(data)).andExpect(
				status().isBadRequest());
	}

	@Test
	public void shouldNotCreateANewEventWithAddressNotFound() throws Exception {

		Event event = new Event();
		event.setName("Event");
		event.setDate(now);
		Address address = new Address();
		address.setId(1234L);
		address.setName("Address");
		event.setAddress(address);

		String data = mapper.writeValueAsString(event);
		mockMvc.perform(post(EVENTS).contentType(MediaType.APPLICATION_JSON).content(data)).andExpect(
				status().isNotFound());
	}
	
	@Test
	public void shouldNotCreateANewEventWithExitingAddress() throws Exception {

		Event event = new Event();
		event.setName("Event");
		event.setDate(now);

		addressRepository.save(address);

		Address address = new Address();
		address.setLatitude(1L);
		address.setLongitude(1L);
		address.setCity(State.SAOPAULO.getNome());
		address.setUf(State.SAOPAULO.getSigla());
		address.setName("Address created on setUp()");
		event.setAddress(address);

		String data = mapper.writeValueAsString(event);
		mockMvc.perform(post(EVENTS).contentType(MediaType.APPLICATION_JSON).content(data)).andExpect(
				status().isConflict());
	}

	@Test
	public void shouldDeleteAEvent() throws Exception {

		Event event = new Event();
		event.setName("New Event");
		event.setDate(now);
		Address address = new Address();
		address.setName("New Address");
		address.setLatitude(991191919L);
		address.setLongitude(38373337L);
		address.setCity(State.SAOPAULO.getNome());
		address.setUf(State.SAOPAULO.getSigla());
		event.setAddress(address);

		Event save = service.save(event);
		mockMvc.perform(delete(EVENTS + "/{id}", save.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(
				status().isOk());
	}

	@Test
	public void shouldDeleteAEventSuchWithAnother() throws Exception {
		Address address = new Address();
		address.setName("New Address");
		address.setLatitude(991191919L);
		address.setLongitude(38373337L);
		address.setUf(State.SAOPAULO.getSigla());
		address.setCity(State.SAOPAULO.getNome());

		Event event = new Event();
		event.setName("ahoshfohsdf");
		event.setDate(now);
		event.setAddress(this.address);
		Event save = service.save(event);

		Event event2 = new Event();
		event2.setName("New Event");
		event2.setDate(now);
		event2.setAddress(address);
		service.save(event2);

		mockMvc.perform(delete(EVENTS + "/{id}", save.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(
				status().isOk());
	}

	@Test
	public void shouldNotDeleteAEvent() throws Exception {
		mockMvc.perform(delete(EVENTS + "/{id}", 8765432).contentType(MediaType.APPLICATION_JSON)).andExpect(
				status().isNotFound());
	}

	@Test
	public void shouldGetAEvent() throws Exception {

		Event event = new Event();
		event.setName("New Event");
		event.setDate(now);
		event.setAddress(address);

		Event save = service.save(event);
		mockMvc.perform(get(EVENTS + "/{id}", save.getId()).contentType(MediaType.APPLICATION_JSON)).andExpect(
				status().isOk());
	}

	@Test
	public void shouldNotGetAEvent() throws Exception {
		mockMvc.perform(get(EVENTS + "/{id}", 8765432L).contentType(MediaType.APPLICATION_JSON)).andExpect(
				status().isNotFound());
	}

	@Test
	public void shouldGetAllEventByPageOrderingByNameAsc() throws Exception {
		String size = String.valueOf(10);
		String page = String.valueOf(0);

		newEventForTest("A", LocalDateTime.of(2016, 01, 20, 0, 0));
		newEventForTest("B", LocalDateTime.of(2016, 01, 21, 0, 0));
		newEventForTest("C", LocalDateTime.of(2016, 01, 22, 0, 0));

		mockMvc.perform(
				get(EVENTS).contentType(MediaType.APPLICATION_JSON).param("page", page).param("size", size)
						.param("sort", "name,asc")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(3))).andExpect(jsonPath("$.content[0].name", is("A")))
				.andExpect(jsonPath("$.content[1].name", is("B"))).andExpect(jsonPath("$.content[2].name", is("C")))
				.andExpect(jsonPath("$.totalElements", is(3)));
	}

	@Test
	public void shouldGetAllEventByPageOrderingByDateDesc() throws Exception {
		String size = String.valueOf(10);
		String page = String.valueOf(0);

		newEventForTest("A", LocalDateTime.of(2016, 01, 20, 0, 0));
		newEventForTest("B", LocalDateTime.of(2016, 01, 21, 0, 0));
		newEventForTest("C", LocalDateTime.of(2016, 01, 22, 0, 0));

		mockMvc.perform(
				get(EVENTS).contentType(MediaType.APPLICATION_JSON).param("page", page).param("size", size)
						.param("sort", "date,desc")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(3))).andExpect(jsonPath("$.content[0].name", is("C")))
				.andExpect(jsonPath("$.content[1].name", is("B"))).andExpect(jsonPath("$.content[2].name", is("A")))
				.andExpect(jsonPath("$.totalElements", is(3)));
	}

	@Test
	public void shouldEditAEventWithAnExistAddress() throws Exception {

		Event event = newEventForTest("A", LocalDateTime.of(2016, 01, 20, 0, 0));

		event.setName("New Name");

		String json = mapper.writeValueAsString(event);

		mockMvc.perform(put(EVENTS).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(event.getId().intValue())))
				.andExpect(jsonPath("$.name", is(event.getName())));
	}

	@Test
	public void shouldNotEditAEventBecauseItIsInvalidForUpdates() throws Exception {
		Event event = new Event();
		event.setName("Event without ID");
		String json = mapper.writeValueAsString(event);
		mockMvc.perform(put(EVENTS).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(
				status().isBadRequest());
	}

	@Test
	public void shouldNotEditAEventBecauseItsNotExist() throws Exception {
		Event event = new Event();
		event.setId(123456L);
		event.setName("Event D");
		String json = mapper.writeValueAsString(event);
		mockMvc.perform(put(EVENTS).contentType(MediaType.APPLICATION_JSON).content(json))
			.andExpect(	status().isNotFound());
	}

	@Test
	public void shouldNotEditAEventWithAnExistingAddress() throws Exception {
		Event event = newEventForTest("Event", LocalDateTime.of(2016, 01, 20, 0, 0));

		Address address = new Address();
		address.setName("New Address that exist");
		address.setLatitude(1L);
		address.setLongitude(1L);
		address.setCity(State.SAOPAULO.getNome());
		address.setUf(State.SAOPAULO.getSigla());
		event.setAddress(address);
		String json = mapper.writeValueAsString(event);

		mockMvc.perform(put(EVENTS).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void shouldNotEditAEventBecauseAddressNotExist() throws Exception {
		Event event = newEventForTest("Event with not existing address", LocalDateTime.of(2016, 01, 20, 0, 0));
		
		Address address = new Address();
		address.setId(1312123312L);;
		event.setAddress(address);
		String json = mapper.writeValueAsString(event);

		mockMvc.perform(put(EVENTS).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isNotFound());
	}

	private Event newEventForTest(String string, LocalDateTime of) {
		Event event = new Event();
		event.setName(string);
		event.setDate(of);
		event.setAddress(address);
		return service.save(event);
	}
}
