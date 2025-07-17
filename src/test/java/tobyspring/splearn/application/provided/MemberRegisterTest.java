package tobyspring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Import(SplearnTestConfiguration.class)
@Transactional
@SpringBootTest
public record MemberRegisterTest(
        MemberRegister memberRegister,
        EntityManager entityManager
) {

    @Test
    @DisplayName("통합테스트")
    void test1() {
        final Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("중복 이메일 실패")
    void test2() {
        final Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("MemberRegisterRequest 유효성 검증 성공")
    void test3() {
        final MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest("valid@email.com", "validNickName", "validPassword");

        assertDoesNotThrow(() -> memberRegister.register(memberRegisterRequest));
    }

    @CsvSource({
            "invalidEmail, nickName, 123456789",
            "valid@email.com, nick, 123456789",
            "valid@email.com, nickName, 12345"
    })
    @ParameterizedTest
    @DisplayName("MemberRegisterRequest 유효성 검증 실패시 ConstraintViolationException을 던진다.")
    void test4(String email, String nickName, String password) {
        final MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest(email, nickName, password);

        assertThatThrownBy(() -> memberRegister.register(memberRegisterRequest))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("activate()는 Member를 activate 상태로 변경한다.")
    void test5() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }
}
