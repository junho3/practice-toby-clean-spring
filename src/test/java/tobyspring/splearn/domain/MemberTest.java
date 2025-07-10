package tobyspring.splearn.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Member")
class MemberTest {

    @Test
    @DisplayName("Member를 생성한다.")
    void test1() {
        var member = new Member("test.app", "test", "password");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("생성자 NULL 체크")
    void test2() {
        assertThatThrownBy(() -> new Member(null, "test", "password"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("activate()는 Member를 활성 상태로 변경한다.")
    void test3() {
        var member = new Member("test.app", "test", "password");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("activate()는 활성 상태인 Member를 활성 상태로 변경이 실패한다.")
    void test4() {
        var member = new Member("test.app", "test", "password");

        member.activate();

        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("deactivate()는 Member를 탈퇴 상태로 변경한다.")
    void test5() {
        var member = new Member("test.app", "test", "password");
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("deactivate()는 활성 상태가 아니라면 탈퇴 상태 변경이 실패한다.")
    void test6() {
        var member = new Member("test.app", "test", "password");

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }
}
