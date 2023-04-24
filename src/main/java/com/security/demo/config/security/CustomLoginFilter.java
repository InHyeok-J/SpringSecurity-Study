package com.security.demo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.demo.config.security.authentication.CustomAuthentication;
import com.security.demo.exception.InvalidateInputException;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Slf4j
public class CustomLoginFilter extends AbstractAuthenticationProcessingFilter {

    private CustomLoginSuccessHandler customLoginSuccessHandler;
    private CustomLoginFailureHandler customLoginFailureHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomLoginFilter(AntPathRequestMatcher defaultFilterProcessesUrl,
        CustomLoginSuccessHandler customLoginSuccessHandler,
        CustomLoginFailureHandler customLoginFailureHandler) {
        super(defaultFilterProcessesUrl);
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.customLoginFailureHandler = customLoginFailureHandler;
    }

    protected CustomLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }


    /*
     *   인증의 시작 지점. filter 로 설정한 matcher 대로 로그인이 시작하게 된다.
     */

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {
        String body = request.getReader().lines()
            .collect(Collectors.joining(System.lineSeparator()));
        log.info(body);

        LoginRequest dto = new ObjectMapper().readValue(body, LoginRequest.class);
//        LoginRequest dto = convert(body);
        System.out.println(dto);

        // 인증 전 토큰을 생성
        CustomAuthentication token = CustomAuthentication.preAuthentication(dto);

        return super.getAuthenticationManager()
            .authenticate(token); // 해당 토큰을 처리할 수 있는 Manager에게 위임한다.
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        // 해당 오버라이드 메소드를 호출하기전에 session에 저장한다.

        log.info("인증 성공");

        SecurityContextHolder.getContext().setAuthentication(authResult);
        this.customLoginSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        log.info("인증 실패");
        this.customLoginFailureHandler.onAuthenticationFailure(request, response, failed);
    }

}
