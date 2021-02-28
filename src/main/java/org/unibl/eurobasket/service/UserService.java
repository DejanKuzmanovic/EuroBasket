package org.unibl.eurobasket.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.eurobasket.model.Role;
import org.unibl.eurobasket.model.User;
import org.unibl.eurobasket.repository.RoleRepository;
import org.unibl.eurobasket.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    private static final String USER_ROLE = "ROLE_USER";
    private static final String PREMIUM_USER_ROLE = "ROLE_PREMIUM_USER";

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public JSONObject getAllUsers() {
        JSONObject result = new JSONObject();
        JSONArray users = new JSONArray();
        userRepository.findAll().forEach(user -> users.put(createUserJsonResponse(user)));
        result.put("users", users);
        return result;
    }

    public JSONObject getUserById(int id) {
        return userRepository.findById(id).map(this::createUserJsonResponse).orElse(null);
    }

    public JSONObject getCurrentUser(User currentUser) {
        return createUserJsonResponse(currentUser);
    }

    public JSONObject createUser(String username, String password) {
        Optional<Role> role = roleRepository.findByName(USER_ROLE);
        if (role.isPresent() && userRepository.findByUsername(username).isEmpty()) {
            User user = new User(username, new BCryptPasswordEncoder().encode(password), new HashSet<>(Collections.singletonList(role.get())));
            userRepository.save(user);
            return createUserJsonResponse(user);
        }
        return null;
    }

    public JSONObject createUserWithRole(String username, String password, String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isPresent() && userRepository.findByUsername(username).isEmpty()) {
            List<Role> roles = new ArrayList<>();
            for (int i = role.get().getId(); i > 0; i--) {
                roles.add(roleRepository.getOne(i));
            }
            User user = new User(username, new BCryptPasswordEncoder().encode(password), new HashSet<>(roles));
            userRepository.save(user);
            return createUserJsonResponse(user);
        }
        return null;
    }

    public JSONObject changePassword(Optional<User> currentUser, String newPassword) {
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
            return createUserJsonResponse(user);
        }
        return null;
    }

    public JSONObject deleteUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            JSONObject response = new JSONObject();
            response.put("deleted user", createUserJsonResponse(user.get()));
            userRepository.delete(user.get());
            return response;
        }
        return null;
    }

    public JSONObject makePremium(User user) {
        Optional<Role> role = roleRepository.findByName(PREMIUM_USER_ROLE);
        if (role.isPresent() && !user.getRoles().contains(role.get())) {
            Set<Role> roles = user.getRoles();
            roles.add(role.get());
            user.setRoles(roles);
            userRepository.save(user);
            return createUserJsonResponse(user);
        }
        return null;
    }

    private JSONObject createUserJsonResponse(User user) {
        JSONObject response = new JSONObject();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("roles", user.getRoles());
        return response;
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
