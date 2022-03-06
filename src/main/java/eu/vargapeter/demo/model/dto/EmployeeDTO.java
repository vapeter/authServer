package eu.vargapeter.demo.model.dto;

import eu.vargapeter.demo.model.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EmployeeDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String mobil;

    private List<Role> roles;
}
