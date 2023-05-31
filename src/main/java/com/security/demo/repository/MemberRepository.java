package com.security.demo.repository;

import com.security.demo.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByUserId(String userId);

    Optional<Member> findByUserId(String userId);
}
