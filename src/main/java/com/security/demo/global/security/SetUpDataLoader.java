package com.security.demo.global.security;

import com.security.demo.entity.Member;
import com.security.demo.entity.Role;
import com.security.demo.repository.MemberRepository;
import com.security.demo.repository.ResourceRepository;
import com.security.demo.repository.RoleRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SetUpDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // ROLE SETUP
        roleExistCheck();

        //ADMIN SETUP
        checkAdmin();
    }

    private void checkAdmin() {
        String ADMIN_ID = "admin";
        String ADMIN_PWD = "qwer1234!";

        if (!memberRepository.existsByUserId(ADMIN_ID)) {

            Member admin = Member.builder()
                .name("어드민")
                .userId(ADMIN_ID)
                .password(passwordEncoder.encode(ADMIN_PWD))
                .email("admin@email.com")
                .build();
            Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException(""));
            admin.addRole(adminRole);
            memberRepository.save(admin);
        }
    }

    private void roleExistCheck() {
        List<List<String>> roles = List.of(
            List.of("ROLE_ADMIN", "어드민 권한"),
            List.of("ROLE_MANAGER", "매니저 권한"),
            List.of("ROLE_USER", "유저 권한")
        );

        for (List<String> roleData : roles) {
            String roleName = roleData.get(0);
            String desc = roleData.get(1);
            if (!roleRepository.existsByRoleName(roleName)) {
                roleRepository.save(new Role(roleName, desc));
            }
        }
    }


}
