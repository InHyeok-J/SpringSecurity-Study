package com.security.demo.config.security;

import com.security.demo.config.security.authentication.CustomAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLoginAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        CustomAuthentication auth = (CustomAuthentication) authentication;

        log.info("provider 호출");
        String userId = auth.getUserId();
        String password = auth.getPasswrod();

        UserDetails userDetails = customUserDetailService.loadUserByUsername(userId);

        if(passwordMatch(userDetails.getPassword(), password)){
            return new CustomAuthentication(userDetails);
        }

        throw new BadCredentialsException("패스워드가 일치하지 않습니다.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthentication.class.isAssignableFrom(authentication); // authentication 객체와 Provider를 매칭한다.
    }

    private boolean passwordMatch(String dbPassword, String input){
        return passwordEncoder.matches(input, dbPassword);
    }
}
