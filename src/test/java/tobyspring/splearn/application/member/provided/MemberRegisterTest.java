package tobyspring.splearn.application.member.provided;

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
import tobyspring.splearn.domain.member.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Import(SplearnTestConfiguration.class)
@Transactional
@SpringBootTest
record MemberRegisterTest(
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
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

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
        Member member = registerMember();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    @DisplayName("deactivate()는 Member를 deactivate 상태로 변경한다.")
    void test6() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    @Test
    @DisplayName("updateInfo() - 성공")
    void test7() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("macbook", "www01234", "자기소개"));

        assertThat(member.getDetail().getProfile().address()).isEqualTo("www01234");
    }

    @Test
    @DisplayName("updateInfo() - 중복 프로필 주소 실패")
    void test8() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("macbook", "www01234", "자기소개"));

        Member member2 = registerMember("newtest@app.com");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        // member2가 member와 동일한 프로필 주소를 사용하려고 할 때 예외 발생
        assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("macmini", "www01234", "자기소개")))
                .isInstanceOf(DuplicateProfileException.class);

        // member2가 다른 프로필 주소를 사용하려고 할 때 성공
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("macmini", "aaa01234", "자기소개"));

        // 기존 프로필 주소로 바꾸는 것 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("macbook", "www01234", "자기소개"));

        // 프로필 주소를 제거하는 것 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("macbook", "", "자기소개"));

        // 프로필 주소 중복 불가능
        assertThatThrownBy(() -> memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("macbook", "aaa01234", "자기소개")))
                .isInstanceOf(DuplicateProfileException.class);
    }

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();

        return member;
    }
}
