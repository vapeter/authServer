package eu.vargapeter.demo.controller;

import eu.vargapeter.demo.config.Translator;
import eu.vargapeter.demo.log.model.AuditDTO;
import eu.vargapeter.demo.log.model.LogType;
import eu.vargapeter.demo.log.service.AuditService;
import eu.vargapeter.demo.model.dto.EmployeeRegister;
import eu.vargapeter.demo.model.dto.UserDTO;
import eu.vargapeter.demo.service.EmployeeService;
import eu.vargapeter.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private UserService userService;
    private AuditService auditService;
    private EmployeeService employeeService;

    @Autowired
    public AdminController(UserService userService, AuditService auditService, EmployeeService employeeService) {
        this.userService = userService;
        this.auditService = auditService;
        this.employeeService = employeeService;
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/logs")
    public List<AuditDTO> getAllAudit() {
        return auditService.getAllLog();
    }

    @PostMapping("/employeeRegister")
    public ResponseEntity createNewEmployee(@Valid EmployeeRegister employeeRegister, BindingResult result) {

        if (result.hasErrors()) {
            auditService.createAudit(
                    Translator.toLocale("log.employeeRegister.errorFields") + result.getFieldError(),
                    LogType.SPRING_ERROR);
            return new ResponseEntity<>(
                    Translator.toLocale("log.employeeRegister.errorFields"),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            employeeService.createNewEmployee(employeeRegister);
            return new ResponseEntity(
                    Translator.toLocale("log.employeeRegister.success"),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            auditService.createAudit(
                    Translator.toLocale("log.employeeRegister.error") + e.getMessage(),
                    LogType.SPRING_ERROR);
            return new ResponseEntity(
                    Translator.toLocale("log.employeeRegister.error") + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
