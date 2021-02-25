package org.unibl.eurobasket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.unibl.eurobasket.model.Role;
import org.unibl.eurobasket.model.User;
import org.unibl.eurobasket.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthenticationService {

    private UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return userRepository.findByUsername(currentUserName);
        }
        return Optional.empty();
    }

    public boolean isAdmin() {
        Optional<User> currentUser = getCurrentUser();
        if (currentUser.isPresent()) {
            for (Role userRole : currentUser.get().getRoles()) {
                if (userRole.getName().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

