package eu.vargapeter.demo.service;

import eu.vargapeter.demo.model.dto.LoginRequest;

public interface LoginService {

    public String loginUser(LoginRequest loginRequest);
}
