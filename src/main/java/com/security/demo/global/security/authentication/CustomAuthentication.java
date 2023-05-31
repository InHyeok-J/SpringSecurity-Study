package com.security.demo.global.security.authentication;

import com.security.demo.global.security.dto.LoginRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomAuthentication extends UsernamePasswordAuthenticationToken {

    /*
    *   principal : 인증 id 값
    *   credentials : 인증을 위한 pwd 값
    *  authorities => 권한
    */

    public CustomAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }


    public CustomAuthentication(UserDetails userDetails){
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    public static CustomAuthentication preAuthentication(LoginRequest request){
        return new CustomAuthentication(request.getUserId(), request.getPassword());
    }

    public String getUserId(){
        return (String)super.getPrincipal();
    }

    public String getPasswrod(){
        return (String)  super.getCredentials();
    }
}
