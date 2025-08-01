package tobyspring.splearn.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @ValueSource(strings = {
            "xxxxx",
            "xxxxx123456",
            "123456",
            ""
    })
    @ParameterizedTest
    @DisplayName("Profile은 15자리 미만 영문과 숫자만 가능")
    void test1 (String address) {
        new Profile(address);
    }

    @ValueSource(strings = {
            "12345678901234567",
            "XXXX",
            "프로필"
    })
    @ParameterizedTest
    @DisplayName("Profile 실패 케이스")
    void test2(String address) {
        assertThatThrownBy(() -> new Profile(address)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("url()")
    void test3() {
        var profile = new Profile("macbook");

        assertThat(profile.url()).isEqualTo("@macbook");
    }
}
