package org.ebankbackend.security.repo;

import org.ebankbackend.security.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    Optional<AppUser> findAppUserByEmail(String email);
    AppUser findAppUserByEmailIgnoreCase(String email);

}
