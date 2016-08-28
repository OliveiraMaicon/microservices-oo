package com.br.oor.service;

import com.br.oor.exception.ValidationException;
import com.br.oor.model.User;
import com.br.oor.repository.LoginRepository;
import com.br.oor.repository.UserRepository;
import com.br.oor.util.CryptographyUtils;
import com.br.oor.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.OptimisticLockException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

/**
 * Created by maiconoliveira on 28/10/15.
 */

@Service("userService")
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Transactional
    public User save(User user) throws ValidationException, InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        LOGGER.info("Starting save method");
        LOGGER.debug(String.format("save(%s)", user.toString()));
        if (loginRepository.emailEquals(user.getLogin().getEmail()) == null) {
            user.getLogin().setPassword(CryptographyUtils.newInstance().encrypt(user.getLogin().getPassword()));
        } else {
            throw new ValidationException(HttpStatus.CONFLICT, MessageUtil.LOGIN_ALREADY, user.getLogin().getClass().getName());
        }
        user.getLogin().setCreateDate(Calendar.getInstance().getTime());
        user.getLogin().setUpdateDate(Calendar.getInstance().getTime());
        user.setCreateDate(Calendar.getInstance().getTime());
        user.setUpdateDate(Calendar.getInstance().getTime());

        return userRepository.save(user);

    }

    public User update(User user) throws ValidationException {
        LOGGER.info("Starting update method");
        LOGGER.debug(String.format("update(%s)", user.toString()));
        if (user.getId() != null) {
            if (userRepository.findById(user.getId()).isPresent()) {
                user.setUpdateDate(Calendar.getInstance().getTime());
            }else{
                LOGGER.error(String.format("[ID = %s] - USER NOT FOUND ", user.getId()));
                throw new ValidationException(HttpStatus.NOT_FOUND, MessageUtil.USER_NOT_FOUND, user.getId().toString());
            }
        } else {
            LOGGER.error(String.format("[%s] - ID IS REQUIRED. ", user.getId()));
            throw new ValidationException(HttpStatus.NOT_ACCEPTABLE, MessageUtil.ID_REQUIRED);
        }


        try {
            return userRepository.save(user);
        } catch (OptimisticLockException e) {
            LOGGER.warn(String.format("[%s] - Version esta desatualizada. ", user.getVersion().toString()));
            throw new ValidationException(HttpStatus.NOT_ACCEPTABLE, MessageUtil.OBJECT_NEED_UPDATE, user.getLogin().getClass().getName());

        }catch (DataIntegrityViolationException e){
            LOGGER.warn(String.format("[%s] - Email ja existe. ", user.getLogin().getEmail()));
            throw new ValidationException(HttpStatus.CONFLICT, MessageUtil.LOGIN_ALREADY, user.getLogin().getClass().getName());
        }

    }

    @Transactional(readOnly = true)
    public User findById(Long id) throws ValidationException {
        LOGGER.info("Starting findById method");
        LOGGER.debug(String.format("findById(%s)", id));
        return userRepository.findById(id).orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND, MessageUtil.USER_NOT_FOUND));

    }

    @Transactional(readOnly = true)
    public Page<User> findAllByPage(Pageable p) {
        LOGGER.info("Starting findAllByPage method");
        LOGGER.debug(String.format("findAllByPage(%s)", p));
        return findAllByPage(p);
    }


    @Transactional(rollbackFor = ValidationException.class)
    public void delete(Long id) throws ValidationException {
        LOGGER.info(String.format("delete(%s)", id));

        try{
            userRepository.delete(id);
        }catch (EmptyResultDataAccessException e){
            throw new ValidationException(HttpStatus.NOT_FOUND, MessageUtil.USER_NOT_FOUND);
        }
    }

}
