package eu.vargapeter.demo.security;

import eu.vargapeter.demo.model.Employee;
import eu.vargapeter.demo.model.User;
import eu.vargapeter.demo.repository.EmployeeRepository;
import eu.vargapeter.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> user = userRepository.findUserByEmail(username);
		Optional<Employee> employee = employeeRepository.findEmployeeByEmail(username);

		if (!user.isPresent() && !employee.isPresent()) {
			throw new UsernameNotFoundException("Could not find user");
		}

		if (user.isPresent()) {
			return new MyUserDetails(user.get());
		} else {
			return new MyEmployeeDetails(employee.get());
		}

	}

}
