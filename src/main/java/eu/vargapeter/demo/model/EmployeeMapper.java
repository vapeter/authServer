package eu.vargapeter.demo.model;

import eu.vargapeter.demo.model.dto.EmployeeDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDTO toEmployeeDTO(Employee employee);

    List<EmployeeDTO> toEmployeeDTOs(List<Employee> employees);

    Employee toEmployee(EmployeeDTO employeeDTO);
}
