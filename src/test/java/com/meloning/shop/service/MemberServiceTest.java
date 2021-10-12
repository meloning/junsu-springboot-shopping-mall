package com.meloning.shop.service;

import com.meloning.shop.dto.MemberFormDto;
import com.meloning.shop.entity.Member;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("melon8372@gmail.com");
        memberFormDto.setName("메로닝");
        memberFormDto.setAddress("서울특별시 관악구 신림동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        // given
        Member member = createMember();

        // when
        Member registeredMember = memberService.register(member);

        // then
        assertThat(member.getEmail()).isEqualTo(registeredMember.getEmail());
        assertThat(member.getName()).isEqualTo(registeredMember.getName());
        assertThat(member.getAddress()).isEqualTo(registeredMember.getAddress());
        assertThat(member.getPassword()).isEqualTo(registeredMember.getPassword());
        assertThat(member.getRole()).isEqualTo(registeredMember.getRole());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest() {
        // given
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.register(member1);

        // when, then
        assertThatThrownBy(() -> memberService.register(member2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 가입된 회원입니다.");

    }
}