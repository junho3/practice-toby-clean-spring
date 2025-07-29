package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @Test
    @DisplayName("Profile은 15자리 미만 영문과 숫자만 가능")
    void test1 () {
        new Profile("xxxxx");
        new Profile("xxxxx");
        new Profile("123456");
    }

    @Test
    @DisplayName("Profile 실패 케이스")
    void test2() {
        assertThatThrownBy(() -> new Profile("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("12345678901234567")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("XXXX")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("프로필")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("url()")
    void test3() {
        var profile = new Profile("macbook");

        assertThat(profile.url()).isEqualTo("@macbook");
    }
}
