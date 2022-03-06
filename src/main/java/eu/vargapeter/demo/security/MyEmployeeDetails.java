package eu.vargapeter.demo.security;

import eu.vargapeter.demo.model.Employee;
import eu.vargapeter.demo.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MyEmployeeDetails implements UserDetails {

	private Employee employee;

	public MyEmployeeDetails(Employee employee) {
		this.employee = employee;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Role> roles = employee.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return employee.getPassword();
	}

	@Override
	public String getUsername() {
		return employee.getName();
	}

	public String getEmail() {
		return employee.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
