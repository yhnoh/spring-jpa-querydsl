package com.querydsl.inflearn.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.inflearn.entity.Member;
import com.querydsl.inflearn.entity.QMember;
import com.querydsl.inflearn.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.querydsl.inflearn.entity.QMember.member;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {


    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before(){
        queryFactory = new JPAQueryFactory(em);
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

    }

    @Test
    public void startJPQL(){
        String qlString = "select m from Member m where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

    @Test
    public void startQueryDsl(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember m = member;

        Member findMember = queryFactory
                .selectFrom(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void paging1(){

        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();


        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    public void paging2(){

        //카운트 쿼리에 필요없는 쿼리가 들어갈 경우 (join, where) 카운트 쿼리는 따로 제작

        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();
        assertThat(results.getTotal()).isEqualTo(4);
        assertThat(results.getResults().size()).isEqualTo(2);

    }

}
