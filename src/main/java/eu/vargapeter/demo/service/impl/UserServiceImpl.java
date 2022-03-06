package eu.vargapeter.demo.service.impl;

import eu.vargapeter.demo.config.Translator;
import eu.vargapeter.demo.exceptions.UserAlreadyExistsException;
import eu.vargapeter.demo.log.model.LogType;
import eu.vargapeter.demo.log.service.AuditService;
import eu.vargapeter.demo.mail.EmailService;
import eu.vargapeter.demo.model.Role;
import eu.vargapeter.demo.model.User;
import eu.vargapeter.demo.model.UserMapper;
import eu.vargapeter.demo.model.dto.UserDTO;
import eu.vargapeter.demo.model.dto.UserRegister;
import eu.vargapeter.demo.repository.UserRepository;
import eu.vargapeter.demo.security.jwt.JwtUtils;
import eu.vargapeter.demo.service.RoleService;
import eu.vargapeter.demo.service.UserService;
import eu.vargapeter.demo.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private RoleService roleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuditService auditService;
    private VerificationTokenService verificationTokenService;
    private EmailService emailService;
    private JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleService roleService
            , @Lazy BCryptPasswordEncoder bCryptPasswordEncoder,
                           AuditService auditService, VerificationTokenService verificationTokenService,
                           EmailService emailService, JwtUtils jwtUtils
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.auditService = auditService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userMapper.toUserDTOs(userRepository.findAll());
    }

    @Override
    public Boolean userExists(String email) {
        return findUserByEmail(email).isPresent();
    }


    @Override
    public void createNewUserFromForm(UserRegister userRegister) {

        if (userExists(userRegister.getEmail())) {
            throw new UserAlreadyExistsException(
                    Translator.toLocale("log.newUser.alreadyExists")
            );
        }

        Optional<User> savedUser = Optional.of(saveUser(userBuilderFromForm(userRegister)));

        savedUser.ifPresent(
                user -> {
                    try {
                        setUserRoleToUser(savedUser.get());
                        String token = UUID.randomUUID().toString();
                        verificationTokenService.saveToken(savedUser.get(), token);
                        emailService.sendVerificationMail(user);

                        auditService.createAudit(Translator.toLocale("log.newUser.success")
                                        + savedUser.get().getEmail(),
                                LogType.CREATED);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }


    private User userBuilderFromForm(UserRegister userRegister) {

        Set<Role> roles = new HashSet<>();

        String encodedPassword = bCryptPasswordEncoder.encode(userRegister.getPassword());

        User newUserFromForm = User.builder()
                .password(encodedPassword)
                .oldUser(false)
                .enabled(false)
                .email(userRegister.getEmail())
                .mobil(userRegister.getMobil())
                .comment(userRegister.getComment())
                .name(userRegister.getName())
                .build();

        newUserFromForm.setRoles(roles);

        return newUserFromForm;
    }

    private void setUserRoleToUser(User user) {

        Role role = roleService.findRoleById(1);
        user.addRole(role);

        this.saveUser(user);
    }

}
