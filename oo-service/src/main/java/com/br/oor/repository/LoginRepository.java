package com.br.oor.repository;

import com.br.oor.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Created by maiconoliveira on 28/10/15.
 */
public interface LoginRepository extends JpaRepository<Login,Long> {

    Login emailEquals(String email);

}