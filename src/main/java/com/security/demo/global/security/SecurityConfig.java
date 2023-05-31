package com.security.demo.global.security;

import com.security.demo.global.security.entrypoint.CustomAuthenticationEntryPoint;
import com.security.demo.global.security.filter.CustomLoginFilter;
import com.security.demo.global.security.handler.CustomLoginFailureHandler;
import com.security.demo.global.security.handler.CustomLoginSuccessHandler;
import com.security.demo.global.security.metadatasource.UrlFilterInvocationSecurityMetaDataSource;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint entryPoint;
    private final CustomLoginSuccessHandler successHandler;
    private final CustomLoginFailureHandler failureHandler;
    private final UrlFilterInvocationSecurityMetaDataSource urlFilterInvocationSecurityMetaDataSource;
    // manager를 가져오기 위한 빈


    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomLoginFilter customFilter(AuthenticationManager authenticationManager) {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/member/login",
            HttpMethod.POST.name());
        CustomLoginFilter customLoginFilter = new CustomLoginFilter(matcher, successHandler,
            failureHandler);

        customLoginFilter.setAuthenticationManager(authenticationManager);

        SecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
        customLoginFilter.setSecurityContextRepository(contextRepository);

        return customLoginFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable.
        http.csrf().disable();

        // 인증 수행 후 결과에 따른 리턴 설정. 실패 시 entry point로 이동한다.
        http.exceptionHandling()
            .authenticationEntryPoint(entryPoint);

        http.authorizeRequests()
            .antMatchers("/", "/test", "/member/sign-up", "/member/login").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated(); //나머지는 인증 검사 진행.

        http.formLogin()
            .disable(); // 스프링 시큐리티 기본 form 로그인을 disable 한다.

        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(
            AuthenticationConfiguration.class)); //http와 config를 공유하는 manager 생성

        CustomLoginFilter loginFilter = customFilter(
            authenticationManager);
        FilterSecurityInterceptor customFilterSecurityInterceptor = customFilterSecurityInterceptor(
            authenticationManager);

        http.addFilterBefore(loginFilter,
            UsernamePasswordAuthenticationFilter.class); // UserName Password Filter 앞에 둠
        http.addFilterBefore(customFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        return http.build();
    }

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor(
        AuthenticationManager authenticationManager) {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(
            urlFilterInvocationSecurityMetaDataSource);
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager);
        return filterSecurityInterceptor;
    }

    private AccessDecisionManager affirmativeBased() {
        AffirmativeBased affirmativeBased = new AffirmativeBased(getAccessDecisionVoters());
        return affirmativeBased;
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        return Arrays.asList(new RoleVoter());
    }
}
