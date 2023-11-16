package org.ebankbackend.service;

import lombok.AllArgsConstructor;
import org.ebankbackend.security.model.AppRole;
import org.ebankbackend.security.model.AppUser;
import org.ebankbackend.security.repo.AppRoleRepository;
import org.ebankbackend.security.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser addUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return userRepository.save(appUser);
    }

    @Override
    public List<AppUser> appUsers() {
        return userRepository.findAll();
    }

    @Override
    public AppRole addRole(AppRole appRole) {
        return roleRepository.save(appRole);
    }

    @Override
    public void setRoleToUser(String email, String rolename) {
        AppUser appUser = userRepository.findAppUserByEmailIgnoreCase(email);
        AppRole appRole = roleRepository.findAppRoleByRolename(rolename);
        appUser.getRoles().add(appRole);
        userRepository.save(appUser);
    }

    @Override
    public void removeRoleFromUser(String email, String rolename) {
        AppUser appUser = userRepository.findAppUserByEmailIgnoreCase(email);
        AppRole appRole = roleRepository.findAppRoleByRolename(rolename);
        appUser.getRoles().remove(appRole);
        userRepository.save(appUser);
    }
}
