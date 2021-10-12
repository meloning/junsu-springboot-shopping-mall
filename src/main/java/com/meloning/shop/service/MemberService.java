package com.meloning.shop.service;

import com.meloning.shop.entity.Member;
import com.meloning.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member register(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(savedMember -> {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        });
        return memberRepository.save(member);
    }
}
