package eu.vargapeter.demo.controller;

import eu.vargapeter.demo.config.Translator;
import eu.vargapeter.demo.log.model.LogType;
import eu.vargapeter.demo.log.service.AuditService;
import eu.vargapeter.demo.model.User;
import eu.vargapeter.demo.model.VerificationToken;
import eu.vargapeter.demo.model.dto.UserRegister;
import eu.vargapeter.demo.service.UserService;
import eu.vargapeter.demo.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Timestamp;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RegisterUserController {


    private UserService userService;
    private AuditService auditService;
    private VerificationTokenService verificationTokenService;


    @Autowired
    public RegisterUserController(UserService userService, AuditService auditService,
                                  VerificationTokenService verificationTokenService
    ) {
        this.userService = userService;
        this.auditService = auditService;
        this.verificationTokenService = verificationTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity createNewUser(@Valid UserRegister userRegister, BindingResult result) {

        if (result.hasErrors()) {
            auditService.createAudit(
                    Translator.toLocale("log.newUser.errorFields") + result.getFieldError(),
                        LogType.SPRING_ERROR
            );
            return new ResponseEntity<>(
                    Translator.toLocale("log.newUser.errorFields"),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            userService.createNewUserFromForm(userRegister);
            return new ResponseEntity(
                    Translator.toLocale("log.newUser.success"),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            auditService.createAudit(
                    Translator.toLocale("log.newUser.error") + e.getMessage(),
                    LogType.SPRING_ERROR);
            return new ResponseEntity(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/activation")
    public ResponseEntity activation(@RequestParam("token") String token) {

        VerificationToken verificationToken = verificationTokenService.findByToken(token);

        if (verificationToken == null) {
            return new ResponseEntity("Érvénytelen token!", HttpStatus.BAD_REQUEST);
        } else {
             User user = verificationToken.getUser();

             if ( !user.getEnabled()) {
                 Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());

                 if (verificationToken.getTimestamp().before(currentTimeStamp)) {
                     return new ResponseEntity("A tokened lejárt!!", HttpStatus.BAD_REQUEST);
                 } else {
                     user.setEnabled(true);

                     userService.saveUser(user);

                     return new ResponseEntity("Sikeres aktiválás!", HttpStatus.OK);
                 }
             }
        }
        return new ResponseEntity("A felhasználó már aktivált!", HttpStatus.OK);
    }
}
