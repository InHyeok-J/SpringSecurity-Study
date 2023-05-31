package com.security.demo.controller.dto;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SignUpMemberRequestTest {


    private String correctUserId = "qwerQWER";
    private String correctPassword = "qwerQWER!1";
    private String correctEmail ="email@eamil.com";
    private String correctName = "킹인혁";

    private Validator validator;
    @BeforeEach
    void init(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory() ;
        validator =factory.getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings ={ "한글a","aaaaa","사이즈가틀린것"})
    @DisplayName("회원가입 요청시 한글이 아닌 값을 넣으면 에러")
    void testNameNotCorrect(String input){
        SignUpMemberRequest memberRequest = new SignUpMemberRequest(correctUserId,
            correctPassword, correctEmail, input);

        Set<ConstraintViolation<SignUpMemberRequest>> validate = validator.validate(
            memberRequest);
        assertFalse(validate.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings ={ "한글임","한글글글","사이즈가맞"})
    @DisplayName("회원가입 요청시 한글 이름, 사이즈 3~5이면 성공한다")
    void testNameCorrect(String input){
        SignUpMemberRequest memberRequest = new SignUpMemberRequest(correctUserId,
            correctPassword, correctEmail, input);

        Set<ConstraintViolation<SignUpMemberRequest>> validate = validator.validate(
            memberRequest);
        assertTrue(validate.isEmpty());
    }


    @DisplayName("패스워드 정규식 영어 대소문자 숫자,특수문자가 포함되지않으면 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"shortLe", "zzzaaaaa12","zxcAAA12355","AAAAAAAAAA1","124523214", "!!!!!!!aaaaa"})
    void testPasswordRegxpFail(String input){
        SignUpMemberRequest memberRequest = new SignUpMemberRequest(correctUserId,
            input, correctEmail, correctName);

        Set<ConstraintViolation<SignUpMemberRequest>> validate = validator.validate(
            memberRequest);
        assertFalse(validate.isEmpty());
    }

    @DisplayName("패스워드 정규식 영어 대소문자 숫자,특수문자가 1개씩 들어가면 성공한다.")
    @ParameterizedTest
    @ValueSource(strings = {"shortLe!2", "zzza@aa12","zxcAAA1@@","!AAAAAAAAA1","123AAzz@#", "!!123aaa@@aA"})
    void testPasswordRegxpSuccess(String input){
        SignUpMemberRequest memberRequest = new SignUpMemberRequest(correctUserId,
            input, correctEmail, correctName);

        Set<ConstraintViolation<SignUpMemberRequest>> validate = validator.validate(
            memberRequest);
        assertTrue(validate.isEmpty());
    }
}