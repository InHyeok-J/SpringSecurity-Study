package com.security.demo.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.security.demo.global.exception.DuplicateResourceException;
import com.security.demo.controller.dto.SignUpMemberRequest;
import com.security.demo.entity.Member;
import com.security.demo.repository.MemberRepository;
import com.security.demo.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;


    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("email 중복 시 에러가 발생한다")
    void throwCheckDuplicateEmail(){
        //given
        String duplicateEmail = "email@email.com";
        given(memberRepository.existsByEmail(duplicateEmail)).willReturn(true);

        assertThatThrownBy(()-> memberService.checkDuplicate(duplicateEmail,null))
            .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("userId 중복 시 에러가 발생한다")
    void throwCheckDuplicateUserId(){
        //given
        String NotduplicateEmail = "email@email.com";
        String duplicateUserID = "userIdss..";
        given(memberRepository.existsByEmail(NotduplicateEmail)).willReturn(false);
        given(memberRepository.existsByUserId(duplicateUserID)).willReturn(true);

        assertThatThrownBy(()-> memberService.checkDuplicate(NotduplicateEmail,duplicateUserID))
            .isInstanceOf(DuplicateResourceException.class);
    }


    @Test
    @DisplayName("회원가입 성공시 save method를 호출한다")
    void signupSuccessCallSaveMethod(){
        SignUpMemberRequest requestDto = new SignUpMemberRequest("userIdssss","pwd1!qwer2","eamil@email.com","김철수");
        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.existsByUserId(any())).willReturn(false);
        given(passwordEncoder.encode(requestDto.getPassword())).willReturn("encodingspasswordss..");

        memberService.signup(requestDto);

        verify(memberRepository,times(1)).save(any(Member.class));
    }
}