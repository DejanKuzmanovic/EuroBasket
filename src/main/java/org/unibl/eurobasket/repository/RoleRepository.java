package org.unibl.eurobasket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.eurobasket.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);

}
