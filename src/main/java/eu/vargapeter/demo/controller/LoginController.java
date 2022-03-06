package eu.vargapeter.demo.controller;

import eu.vargapeter.demo.config.Translator;
import eu.vargapeter.demo.model.dto.LoginRequest;
import eu.vargapeter.demo.security.AuthResponse;
import eu.vargapeter.demo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        String token;

        try {
            token = loginService.loginUser(loginRequest);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    Translator.toLocale("log.loginError"),
                    HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
