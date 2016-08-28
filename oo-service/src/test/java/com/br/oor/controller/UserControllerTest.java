package com.br.oor.controller;

import com.br.oor.Application;
import com.br.oor.model.User;
import com.br.oor.repository.UserRepository;
import com.br.oor.util.MessageUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Calendar;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by maiconoliveira on 14/12/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("integration")
@IntegrationTest("server.port:0")
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    private static final String USERS = "/users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EmbeddedWebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private User user;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        user = new User();
        user.setName("Teste teste teste");
        user.setBirthDate(Calendar.getInstance().getTime());
        user.setUpdateDate(Calendar.getInstance().getTime());
        user.getLogin().setEmail("teste@teste.com.br");
        user.getLogin().setPassword("1234567");

    }


    @Test
    public void nameIsRequired() throws Exception {
        user = new User();
        user.getLogin().setEmail("teste@teste.com.br");
        user.getLogin().setPassword("blablabla");
        String data = mapper.writeValueAsString(user);
        mockMvc.perform(post(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field", is("name")))
                .andExpect(jsonPath("$.message", is("Nome obrigatorio")));

    }


    @Test
    public void emailIsRequired() throws Exception {
        user = new User();
        user.setName("teste");
        user.getLogin().setPassword("blablabla");
        String data = mapper.writeValueAsString(user);
        mockMvc.perform(post(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field", is("login.email")))
                .andExpect(jsonPath("$.message", is(MessageUtil.EMAIL_REQUIRED)));

    }


    @Test
    public void invalidEmail() throws Exception {
        user = new User();
        user.setName("teste");
        user.getLogin().setPassword("blablabla");
        user.getLogin().setEmail("teste.com.br");
        String data = mapper.writeValueAsString(user);
        mockMvc.perform(post(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field", is("login.email")))
                .andExpect(jsonPath("$.message", is(MessageUtil.INVALID_EMAIL)));

    }


    @Test
    public void saveSuccess() throws Exception {

        user = new User();
        user.setName("Teste teste teste");
        user.setBirthDate(Calendar.getInstance().getTime());
        user.getLogin().setEmail("teste@teste.com.br");
        user.getLogin().setPassword("1234567");

        String data = mapper.writeValueAsString(user);
        mockMvc.perform(post(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isCreated());
    }


    @Test
    public void updateSuccess() throws Exception {

        String name = "Update teste";
        String email = "update@teste.com.br";
        String password = "7654321";

        user = userRepository.save(user);
        user.setName(name);
        user.getLogin().setEmail(email);
        user.getLogin().setPassword(password);

        String data = mapper.writeValueAsString(user);
        mockMvc.perform(put(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.login.email", is(email)))
                .andExpect(jsonPath("$.login.password", is(password)));


    }


    @Test
    public void userNoExists() throws Exception {


        user = userRepository.save(user);
        user.setId(23L);

        String data = mapper.writeValueAsString(user);
        mockMvc.perform(put(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(MessageUtil.USER_NOT_FOUND)));

    }


    @Test
    public void userIdRequiredForUpdate() throws Exception {

        String data = mapper.writeValueAsString(user);
        mockMvc.perform(put(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message", is(MessageUtil.ID_REQUIRED)));

    }

    @Test
    public void deleteIdNotFound() throws Exception {

        mockMvc.perform(delete(USERS + "/{id}", 1l)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());


    }


    @Test
    public void deleteSuccess() throws Exception {

        user = userRepository.save(user);

        mockMvc.perform(delete(USERS + "/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());


    }


}
