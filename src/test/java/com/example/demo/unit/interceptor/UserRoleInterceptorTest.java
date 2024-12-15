package com.example.demo.unit.interceptor;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.controller.ReservationController;
import com.example.demo.dto.Authentication;
import com.example.demo.entity.Role;
import com.example.demo.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class UserRoleInterceptorTest {

    @MockitoBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void preHandle() throws Exception {
        mockMvc.perform(post("/reservations"))
                .andExpect(status().isUnauthorized());

        MockHttpSession session = new MockHttpSession();
        Authentication adminAuthentication = new Authentication(1L, Role.ADMIN);
        session.setAttribute(GlobalConstants.USER_AUTH, adminAuthentication);

        mockMvc.perform(post("/reservations")
                        .session(session))
                .andExpect(status().isUnauthorized());

        Authentication userAuthentication = new Authentication(1L, Role.USER);
        session.setAttribute(GlobalConstants.USER_AUTH, userAuthentication);

        mockMvc.perform(post("/reservations")
                        .session(session))
                .andExpect(status().isBadRequest());
        
    }
}