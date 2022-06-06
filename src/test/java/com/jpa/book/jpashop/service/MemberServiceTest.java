package com.jpa.book.jpashop.service;

import com.jpa.book.jpashop.domain.Member;
import com.jpa.book.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {


    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;


    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복회원예외() throws Exception {
        // given
        String name = "Test";
        Member member1 = new Member();
        member1.setName(name);

        Member member2 = new Member();
        member2.setName(name);

        // when
        memberService.join(member1);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }

    @Test
    public void findOne() throws Exception {
        // given

        // when

        // then
    }
}