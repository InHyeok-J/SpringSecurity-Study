package com.security.demo.member.repository;

import com.security.demo.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByUserId(String email);

    Optional<Member> findByUserId(String userId);
}
