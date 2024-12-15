package com.example.demo.unit.interceptor;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.controller.AdminController;
import com.example.demo.dto.Authentication;
import com.example.demo.entity.Role;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.interceptor.AdminRoleInterceptor;
import com.example.demo.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminRoleInterceptorTest {

    @MockitoBean
    private AdminService adminService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void preHandle() throws Exception {

        mockMvc.perform(post("/admins/report-users")
                        .with(request -> {
                            request.setSession(null);
                            return request;
                        }))
                .andExpect(status().isUnauthorized());

        MockHttpSession session = new MockHttpSession();
        Authentication userAuth = new Authentication(1L, Role.USER);
        Authentication adminAuth = new Authentication(1L, Role.ADMIN);
        session.setAttribute(GlobalConstants.USER_AUTH, userAuth);

        mockMvc.perform(post("/admins/report-users")
                        .session(session))
                .andExpect(status().isUnauthorized());

        session.setAttribute(GlobalConstants.USER_AUTH, adminAuth);

        mockMvc.perform(post("/admins/report-users")
                        .session(session))
                .andExpect(status().isBadRequest());

    }

    @Test
    void sessionNullTest() {
        AdminRoleInterceptor interceptor = new AdminRoleInterceptor();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession(false)).thenReturn(null);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> interceptor.preHandle(request, mock(HttpServletResponse.class), new Object())
        );

        assertTrue(ex.getMessage().contains("세션이 끊어졌습니다."));
    }
}