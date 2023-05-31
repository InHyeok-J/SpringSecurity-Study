package com.security.demo.repository;

import com.security.demo.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByRoleName(String rolename);
    Optional<Role> findByRoleName(String roleName);
}
