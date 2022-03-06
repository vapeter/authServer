package eu.vargapeter.demo.model.dto;

import eu.vargapeter.demo.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private String name;

    private String email;

    private String mobil;

    private Boolean oldUser;

    private String comment;

    private List<Role> roles;

}
