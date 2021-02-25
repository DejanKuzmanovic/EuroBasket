package org.unibl.eurobasket.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.unibl.eurobasket.model.Role;
import org.unibl.eurobasket.model.User;
import org.unibl.eurobasket.repository.RoleRepository;
import org.unibl.eurobasket.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class InitialDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private static final String MODERATOR_ROLE = "ROLE_MODERATOR";
    private static final String PREMIUM_USER_ROLE = "ROLE_PREMIUM_USER";
    private static final String USER_ROLE = "ROLE_USER";

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        createRoles();
        createUserWithRole("admin", "admin", Arrays.asList(USER_ROLE, PREMIUM_USER_ROLE, MODERATOR_ROLE, ADMIN_ROLE));
        createUserWithRole("moderator", "moderator", Arrays.asList(USER_ROLE, PREMIUM_USER_ROLE, MODERATOR_ROLE));
        createUserWithRole("premium", "premium", Arrays.asList(USER_ROLE, PREMIUM_USER_ROLE));
        createUserWithRole("user", "user", Collections.singletonList(USER_ROLE));
    }

    private void createUserWithRole(String username, String password, List<String> roleList) {
        if (userRepository.findByUsername(username).isEmpty()) {
            Set<Role> roles = roleList.stream().filter(x -> roleRepository.findByName(x).isPresent()).map(x -> roleRepository.findByName(x).get()).collect(Collectors.toSet());
            User user = new User(username, new BCryptPasswordEncoder().encode(password), roles);
            userRepository.save(user);
        }
    }

    private void createRoles() {
        if (roleRepository.findByName(ADMIN_ROLE).isEmpty()) {
            Role role = new Role(ADMIN_ROLE);
            roleRepository.save(role);
        }

        if (roleRepository.findByName(MODERATOR_ROLE).isEmpty()) {
            Role role = new Role(MODERATOR_ROLE);
            roleRepository.save(role);
        }

        if (roleRepository.findByName(PREMIUM_USER_ROLE).isEmpty()) {
            Role role = new Role(PREMIUM_USER_ROLE);
            roleRepository.save(role);
        }

        if (roleRepository.findByName(USER_ROLE).isEmpty()) {
            Role role = new Role(USER_ROLE);
            roleRepository.save(role);
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}