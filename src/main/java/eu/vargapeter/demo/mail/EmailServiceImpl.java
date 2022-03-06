package eu.vargapeter.demo.mail;

import eu.vargapeter.demo.model.User;
import eu.vargapeter.demo.model.VerificationToken;
import eu.vargapeter.demo.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username:}")
    private String NOREPLY_ADDRESS;

    @Value("${app.mail.verficationURL:}")
    private String URL;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendVerificationMail(User user) {

        VerificationToken verificationToken = verificationTokenService.findByUser(user);
        String token = verificationToken.getToken();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Kedves " + user.getName() + "!");
        stringBuilder.append("\n");
        stringBuilder.append("Köszönjük regisztrációdat! \n");
        stringBuilder.append("Kérlek az alábbi linkre kattintva aktiváld email címedet:");
        stringBuilder.append("\n");
        stringBuilder.append(URL + token);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(user.getEmail());
            message.setSubject("Verification");
            message.setText(stringBuilder.toString());
            emailSender.send(message);

        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
