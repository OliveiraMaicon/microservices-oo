package com.br.oor.service;

import com.br.oor.Application;
import com.br.oor.exception.ValidationException;
import com.br.oor.model.Login;
import com.br.oor.model.User;
import com.br.oor.repository.LoginRepository;
import com.br.oor.repository.UserRepository;
import com.br.oor.util.MessageUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;

import javax.persistence.OptimisticLockException;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by maiconoliveira on 23/12/15.
 */
@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private UserRepository userRepository;

    private User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test(expected = ValidationException.class)
    public void uniqueEmailExeption() throws Exception {
        user = new User();
        user.setName("Teste");
        Login login = new Login();
        user.getLogin().setEmail("Teste");

        when(loginRepository.emailEquals(user.getLogin().getEmail())).thenReturn(login);

        verify(userService.save(user));
    }

    @Test
    public void userNoExistIdRequired() {

        user = new User();
        user.setId(null);
        try {
            userService.update(user);

        } catch (ValidationException ex) {
            Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, ex.getHttpStatus());
            Assert.assertEquals(MessageUtil.ID_REQUIRED, ex.getMessage());
        }


    }

    @Test
    public void userNotFound() {

        user = new User();
        user.setId(3L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        try {
            userService.update(user);

        } catch (ValidationException ex) {
            Assert.assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
            Assert.assertEquals(MessageUtil.USER_NOT_FOUND, ex.getMessage());
        }

    }



    @Test
    public void olderVersion(){

        user = new User();
        user.setId(3L);
        user.setVersion(0L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(new User()));
        when(userRepository.save(user)).thenThrow(OptimisticLockException.class);
        try {
            userService.update(user);
            Assert.assertTrue(Boolean.FALSE);
        } catch (ValidationException e) {
            Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, e.getHttpStatus());
            Assert.assertEquals(MessageUtil.OBJECT_NEED_UPDATE, e.getMessage());

        }

    }
}
