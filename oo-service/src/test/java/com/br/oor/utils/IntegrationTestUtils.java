package com.br.oor.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.oor.repository.AddressRepository;
import com.br.oor.repository.EventRepository;

@Service
public class IntegrationTestUtils {

	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
}
