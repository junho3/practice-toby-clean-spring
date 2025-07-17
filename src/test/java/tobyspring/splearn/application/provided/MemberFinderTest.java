package tobyspring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(SplearnTestConfiguration.class)
@Transactional
@SpringBootTest
record MemberFinderTest(
        MemberFinder memberFinder,
        MemberRegister memberRegister,
        EntityManager entityManager
) {

    @Test
    @DisplayName("find()")
    void test1() {
        final Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        final Member actual = memberFinder.find(member.getId());
        
        assertThat(member.getId()).isEqualTo(actual.getId());
    }

    @Test
    @DisplayName("find() 실패")
    void test2() {
        assertThatThrownBy(() -> memberFinder.find(9999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
