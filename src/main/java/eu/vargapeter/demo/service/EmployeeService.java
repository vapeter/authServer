package eu.vargapeter.demo.service;

import eu.vargapeter.demo.model.Employee;
import eu.vargapeter.demo.model.dto.EmployeeDTO;
import eu.vargapeter.demo.model.dto.EmployeeRegister;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    Optional<Employee> findEmployeeByEmail(String email);

    Boolean employeeExist(String email);

    List<EmployeeDTO> getAllEmployees();

    void createNewEmployee(EmployeeRegister employeeRegister);

}
