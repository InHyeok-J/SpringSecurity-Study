package com.security.demo.member.service;

import com.security.demo.member.controller.dto.SignUpMemberRequest;
import com.security.demo.member.entity.Member;
import com.security.demo.exception.DuplicateResourceException;
import com.security.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public void checkDuplicate(String email, String userId){
        if(email != null && memberRepository.existsByEmail(email)){
            throw new DuplicateResourceException(email+" 이 중복됨");
        }

        if(userId != null && memberRepository.existsByUserId(userId)){
            throw new DuplicateResourceException(userId +" 이 중복됨");
        }
    }

    @Transactional
    public void signup(SignUpMemberRequest registRequest){
        checkDuplicate(registRequest.getEmail(), registRequest.getUserId());
        Member preMember = registRequest.toEntity();
        preMember.updatePassword(passwordEncoder.encode(registRequest.getPassword()));
        memberRepository.save(preMember);
    }
}
