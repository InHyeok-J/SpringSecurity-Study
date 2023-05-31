package com.security.demo.repository;

import com.security.demo.entity.Resource;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Optional<Resource> findByResourceNameAndHttpMethod(String name, String method);
}
