package org.ebankbackend.security.repo;

import org.ebankbackend.security.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {

    AppRole findAppRoleByRolename(String rolename);
}
