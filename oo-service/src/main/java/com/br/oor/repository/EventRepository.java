package com.br.oor.repository;

import com.br.oor.model.Event;
import com.br.oor.model.form.EventSearchForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by agomes on 15/01/16.
 */
public interface EventRepository extends JpaRepository<Event, Long>{

	Optional<Event> findById(Long id);

	@Query(value = "select new com.br.oor.model.form.EventSearchForm(e.id, e.name, e.address.name, e.address.city) " +
			"from Event  e where e.name like %:fieldSearch% " +
			"and e.address.name like %:fieldSearch% " +
			"and e.address.city like %:fieldSearch% " +
			"and e.date = :date")
	List<EventSearchForm> search(@Param("fieldSearch") String fieldSearch , @Param("date") LocalDateTime date);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM event", nativeQuery = true)
	void deleteForTests();

	@Query(value = "select new com.br.oor.model.form.EventSearchForm(e.id, e.name, e.address.name, e.address.city) " +
			"from Event  e where e.date = ?0")
	List<EventSearchForm> findByEventDate(LocalDateTime date);
}
