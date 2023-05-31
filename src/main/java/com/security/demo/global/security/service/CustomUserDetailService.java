package com.security.demo.global.security.service;

import com.security.demo.entity.Member;
import com.security.demo.repository.MemberRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(username)
            .orElseThrow(()-> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

        return createDetail(member);
    }

    private UserDetails createDetail(Member member){
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        // String username, String password, Collection<? extends GrantedAuthority> authorities)
        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}
