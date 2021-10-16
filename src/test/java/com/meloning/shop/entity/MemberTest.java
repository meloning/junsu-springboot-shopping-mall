package com.meloning.shop.entity;

import com.meloning.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("Auditing Test")
    @WithMockUser(username = "junsu", roles = "USER")
    public void auditingTest() {
        // given
        Member newMember = new Member();
        memberRepository.save(newMember);

        entityManager.flush();
        entityManager.clear();

        // when
        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);

        // then
        System.out.println(String.format("register time: %s", member.getCreatedDate()));
        System.out.println(String.format("update time: %s", member.getUpdatedDate()));
        System.out.println(String.format("create member: %s", member.getCreatedBy()));
        System.out.println(String.format("modify member: %s", member.getModifiedBy()));
    }

}
