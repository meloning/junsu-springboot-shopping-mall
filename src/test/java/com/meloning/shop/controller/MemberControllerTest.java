package com.meloning.shop.controller;

import com.meloning.shop.dto.MemberFormDto;
import com.meloning.shop.entity.Member;
import com.meloning.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class MemberControllerTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("장준수");
        memberFormDto.setAddress("서울시 관악구 신림동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.register(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        // given
        final String testEmail = "melon8372@gmail.com";
        final String testPassword = "1234";
        this.createMember(testEmail, testPassword);

        // when
        ResultActions resultActions = mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(testEmail)
                .password(testPassword)
        );

        // then
        resultActions.andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        // given
        final String testEmail = "melon8372@gmail.com";
        final String testPassword = "1234";
        this.createMember(testEmail, testPassword);

        // when
        ResultActions resultActions = mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(testEmail)
                .password("12345")
        );

        // then
        resultActions.andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }
}
