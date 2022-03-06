package eu.vargapeter.demo.model.dto;

import eu.vargapeter.demo.model.Role;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDTO {

    private String name;

    private String email;

    private String mobil;

    private List<Role> roles;
}
