package com.br.oor.repository;

import com.br.oor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by maiconoliveira on 28/10/15.
 */
public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findById(Long id);
}
