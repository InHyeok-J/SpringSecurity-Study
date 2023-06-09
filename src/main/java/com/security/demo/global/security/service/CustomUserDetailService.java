package com.security.demo.global.security.service;

import com.security.demo.entity.Member;
import com.security.demo.entity.MemberRole;
import com.security.demo.repository.MemberRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserIdAndAllRole(username)
            .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

        return createDetail(member);
    }

    private UserDetails createDetail(Member member) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<MemberRole> memberRoles = member.getMemberRoles();
        memberRoles.forEach(memberRole -> grantedAuthorities.add(
            new SimpleGrantedAuthority(memberRole.getRole().getRoleName())));
        // String username, String password, Collection<? extends GrantedAuthority> authorities)
        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}
