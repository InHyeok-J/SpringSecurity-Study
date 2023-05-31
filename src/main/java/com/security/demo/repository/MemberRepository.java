package com.security.demo.repository;

import com.security.demo.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByUserId(String userId);

    Optional<Member> findByUserId(String userId);

    @Query("select m from Member m join fetch m.memberRoles where m.userId = :userId")
    Optional<Member> findByUserIdAndAllRole(@Param(value = "userId") String userId);
}
