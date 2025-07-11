package tobyspring.splearn.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Member")
class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };

        this.member = Member.create("test.app", "test", "password", passwordEncoder);
    }

    @Test
    @DisplayName("Member를 생성한다.")
    void test1() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Disabled("SpotBug 플러그인에서 Nullable을 체크하여 Disabled 처리")
    @Test
    @DisplayName("생성자 NULL 체크")
    void test2() {
        assertThatThrownBy(() -> Member.create(null, "test", "password", passwordEncoder))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("activate()는 Member를 활성 상태로 변경한다.")
    void test3() {
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("activate()는 활성 상태인 Member를 활성 상태로 변경이 실패한다.")
    void test4() {
        member.activate();

        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("deactivate()는 Member를 탈퇴 상태로 변경한다.")
    void test5() {
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("deactivate()는 활성 상태가 아니라면 탈퇴 상태 변경이 실패한다.")
    void test6() {
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("verifyPassword()")
    void test7() {
        assertThat(member.verifyPassword("password", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("fail", passwordEncoder)).isFalse();
    }

    @Test
    @DisplayName("changeNickname()")
    void test8() {
        assertThat(member.getNickname()).isEqualTo("test");

        member.changeNickname("apple");

        assertThat(member.getNickname()).isEqualTo("apple");
    }

    @Test
    @DisplayName("changePassword()")
    void test9() {
        member.changePassword("newPassword", passwordEncoder);

        assertThat(member.verifyPassword("newPassword", passwordEncoder)).isTrue();
    }

    @Test
    @DisplayName("ACTIVE 상태 체크")
    void test10() {
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }
}
