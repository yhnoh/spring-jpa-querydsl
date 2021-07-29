package com.querydsl.inflearn.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
@Commit
public class MemberTest {
    @Autowired
    EntityManager em;

    @BeforeEach
    public void before(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("")
    public void selectMember(){
        List<Member> findAllMember = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : findAllMember) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam() = " + member.getTeam().getName());
        }


    }



}
