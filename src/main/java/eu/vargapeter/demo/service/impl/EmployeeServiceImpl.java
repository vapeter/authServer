package eu.vargapeter.demo.service.impl;

import eu.vargapeter.config.Translator;
import eu.vargapeter.demo.exceptions.UserAlreadyExistsException;
import eu.vargapeter.demo.log.model.LogType;
import eu.vargapeter.demo.log.service.AuditService;
import eu.vargapeter.demo.model.Employee;
import eu.vargapeter.demo.model.EmployeeMapper;
import eu.vargapeter.demo.model.Role;
import eu.vargapeter.demo.model.dto.EmployeeDTO;
import eu.vargapeter.demo.model.dto.EmployeeRegister;
import eu.vargapeter.demo.repository.EmployeeRepository;
import eu.vargapeter.demo.service.EmployeeService;
import eu.vargapeter.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;
    private RoleService roleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuditService auditService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper,
                               RoleService roleService,
                               @Lazy BCryptPasswordEncoder bCryptPasswordEncoder,
                               AuditService auditService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.auditService = auditService;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> findEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }

    @Override
    public Boolean employeeExist(String email) {
        return findEmployeeByEmail(email).isPresent();
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeMapper.toEmployeeDTOs(employeeRepository.findAll());
    }

    @Override
    public void createNewEmployee(EmployeeRegister employeeRegister) {

        if (employeeExist(employeeRegister.getEmail())) {
            throw new UserAlreadyExistsException(
                    Translator.toLocale("log.newUser.alreadyExists")
            );
        }

        Optional<Employee> savedEmployee = Optional.of(saveEmployee(employeeBuilder(employeeRegister)));

        savedEmployee.ifPresent(
                employee -> {
                    try {
                        setRoleForNewEmployee(savedEmployee.get());
                        auditService.createAudit(
                                Translator.toLocale("log.employeeRegister.success")
                                        + savedEmployee.get().getName(),
                                LogType.CREATED);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

    }

    private Employee employeeBuilder(EmployeeRegister employeeRegister) {

        Set<Role> roles = new HashSet<>();

        String encodedPassword = bCryptPasswordEncoder.encode(employeeRegister.getPassword());

        Employee newEmployee = Employee.builder()
                .email(employeeRegister.getEmail())
                .password(encodedPassword)
                .mobil(employeeRegister.getMobil())
                .name(employeeRegister.getName())
                .build();

        newEmployee.setRoles(roles);

        return newEmployee;
    }

    private void setRoleForNewEmployee(Employee employee) {

        Role role = roleService.findRoleById(0);
        employee.addRole(role);

        saveEmployee(employee);
    }
}
