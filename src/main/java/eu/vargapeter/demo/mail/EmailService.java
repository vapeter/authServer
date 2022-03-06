package eu.vargapeter.demo.mail;


import eu.vargapeter.demo.model.User;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendVerificationMail(User user);

}
