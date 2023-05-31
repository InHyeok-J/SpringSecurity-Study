package com.security.demo.repository;

import com.security.demo.entity.Member;
import com.security.demo.entity.MemberRole;
import com.security.demo.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole,Long> {

    Optional<MemberRole> findByMemberAndRole(Member member, Role role);
}
