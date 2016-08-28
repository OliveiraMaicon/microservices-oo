package com.br.oor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.br.oor.model.Address;

/**
 * Created by agomes on 17/01/16.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

	Optional<Address> findById(Long id);
}
