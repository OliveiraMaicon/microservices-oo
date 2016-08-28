package com.br.oor.controller;

import com.br.oor.exception.ValidationException;
import com.br.oor.model.User;
import com.br.oor.service.UserService;
import com.br.oor.util.CryptographyUtils;
import com.br.oor.util.MessageError;
import com.br.oor.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by maiconoliveira on 28/10/15.
 */
@RestController
@RequestMapping("users")
public class UserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody @Valid User user, BindingResult result) throws Exception{

        if(result != null && result.hasErrors()){
            LOGGER.error("Objeto invalido");
            return new ResponseEntity<MessageError>(new MessageError(result.getFieldError().getField(),result.getFieldError().getDefaultMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<User>(userService.save(user), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody User user) throws Exception {
        return new ResponseEntity<User>(userService.update(user), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<User>> findAllByPage(Pageable p) throws Exception {
        return new ResponseEntity<Page<User>>(userService.findAllByPage(p), HttpStatus.OK);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity<User> findbyId(@PathVariable Long id) throws Exception {
        return new ResponseEntity<User>(userService.findById(id), HttpStatus.OK);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        userService.delete(id);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);

    }
}
