package eu.vargapeter.demo.service.impl;

import eu.vargapeter.demo.model.dto.LoginRequest;
import eu.vargapeter.demo.security.jwt.JwtUtils;
import eu.vargapeter.demo.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager,
                            JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String loginUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("logged user: " + authentication);

        return jwtUtils.generateJwtToken(authentication);

    }
}
