package com.example.demo.controller;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResDto;
import com.example.demo.dto.common.CommonResDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CommonResDto<UserResDto>> signupWithEmail(@RequestBody UserRequestDto userRequestDto) {
        User user = userService.signupWithEmail(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>("signup", new UserResDto(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResDto<Authentication>> loginWithEmail(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest request
    ) {
        Authentication authentication = userService.loginUser(loginRequestDto);
        HttpSession session = request.getSession();
        session.setAttribute(GlobalConstants.USER_AUTH, authentication);

        return ResponseEntity.ok().body(new CommonResDto<>("login", authentication));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }
}
