package org.unibl.eurobasket.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.unibl.eurobasket.model.Role;
import org.unibl.eurobasket.model.User;
import org.unibl.eurobasket.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service("userDetailsService")
class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User loadedUser = user.get();
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            Set<Role> userRoles = loadedUser.getRoles();
            for (Role role : userRoles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return new org.springframework.security.core.userdetails.User(loadedUser.getUsername(), loadedUser.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
