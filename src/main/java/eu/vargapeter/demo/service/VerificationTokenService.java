package eu.vargapeter.demo.service;


import eu.vargapeter.demo.model.User;
import eu.vargapeter.demo.model.VerificationToken;

public interface VerificationTokenService {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    void saveToken(User user, String token);
}
