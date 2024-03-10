package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() {
        //save
        Member member = new Member("memberV100", 10000);
        try {
            repository.save(member);
        } catch (SQLException e) {
            log.info("db save failed", e);
            //throw new RuntimeException(e);
        }

        //findById
        Member findMember = null;
        try {
            findMember = repository.findById(member.getMemberId());
        } catch (SQLException e) {
            log.info("db findById failed", e);
            //throw new RuntimeException(e);
        }
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);

        //update: money 10000 -> 20000
        Member updateMember;
        try {
            repository.update(member.getMemberId(),20000);
            updateMember = repository.findById(member.getMemberId());
            assertThat(updateMember.getMoney()).isEqualTo(20000);
        } catch (SQLException e) {
            log.info("db update failed", e);
            //throw new RuntimeException(e);
        }

        //delete
        try {
            repository.delete(member.getMemberId());
            assertThatThrownBy( () -> repository.findById(member.getMemberId()))
                        .isInstanceOf(NoSuchElementException.class);
        } catch (SQLException e) {
            log.info("db delete failed", e);
        }
    }
}