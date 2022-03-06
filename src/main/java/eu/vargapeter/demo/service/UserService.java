package eu.vargapeter.demo.service;

import eu.vargapeter.demo.model.User;
import eu.vargapeter.demo.model.dto.UserDTO;
import eu.vargapeter.demo.model.dto.UserRegister;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public User saveUser(User user);

    public Optional<User> findUserByEmail(String email);

    public Boolean userExists(String email);

    public List<UserDTO> getAllUsers();

    public void createNewUserFromForm(UserRegister userRegister);
}
