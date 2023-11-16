package org.ebankbackend.service;

import org.ebankbackend.security.model.AppRole;
import org.ebankbackend.security.model.AppUser;

import java.util.List;

public interface UserSecurityService {

    AppUser addUser(AppUser appUser);
    List<AppUser> appUsers();
    AppRole addRole(AppRole appRole);
    void setRoleToUser(String email , String rolename);
    void removeRoleFromUser(String email , String rolename);
}
