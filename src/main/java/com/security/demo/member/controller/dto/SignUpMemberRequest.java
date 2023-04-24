package com.security.demo.member.controller.dto;

import com.security.demo.member.entity.Member;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpMemberRequest {

    @NotBlank
    @Pattern(regexp = "[a-zA-Z]{8,12}")
    private String userId;

    @NotBlank
    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}")
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "[가-힣]{3,5}")
    private String name;


    public Member toEntity(){
        return Member.builder()
            .email(email)
            .userId(userId)
            .name(name)
            .build();
    }
}
