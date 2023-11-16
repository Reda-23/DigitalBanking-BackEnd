package org.ebankbackend.security;

import lombok.RequiredArgsConstructor;
import org.ebankbackend.security.model.AppUser;
import org.ebankbackend.security.repo.AppUserRepository;
import org.ebankbackend.security.repo.UserDetailsCONFIG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private  AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(username);
                return appUser.map(UserDetailsCONFIG::new).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
