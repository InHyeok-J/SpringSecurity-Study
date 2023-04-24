package com.security.demo.member.controller;

import com.security.demo.member.controller.dto.SignUpMemberRequest;
import com.security.demo.member.service.MemberService;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/duplicate")
    public ResponseEntity<?> checkDuplicateIdAndEmail(
        @RequestParam(value = "userId", required = false) String userId,
        @RequestParam(value = "email", required = false) String email) {
        memberService.checkDuplicate(email, userId);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpMemberRequest request) {
        memberService.signup(request);
        return ResponseEntity.ok("가입 성공");
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal Object princial , HttpSession session){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        System.out.println(princial);
        System.out.println(authentication);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/test")
    public ResponseEntity<?> getUserInfo2(@AuthenticationPrincipal Object princial){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        System.out.println(princial);
        System.out.println(authentication);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.ok("로그아웃 성공");
    }
}
