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
    @DisplayName("Member를 활성 상태로 변경한다.")
    void test3() {
        var member = new Member("test.app", "test", "password");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("활성 상태인 Member를 활성 상태로 변경이 실패한다.")
    void test4() {
        var member = new Member("test.app", "test", "password");

        member.activate();

        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }
}
