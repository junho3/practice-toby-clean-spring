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
}
