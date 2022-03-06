package eu.vargapeter.demo.service.impl;

import eu.vargapeter.demo.model.Role;
import eu.vargapeter.demo.repository.RoleRepository;
import eu.vargapeter.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleById(Integer roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }
}
