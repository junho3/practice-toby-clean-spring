package tobyspring.splearn.adapter.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SecurePasswordEncoder")
class SecurePasswordEncoderTest {

    @Test
    @DisplayName("패스워드 검증")
    void test1() {
        final SecurePasswordEncoder sut = new SecurePasswordEncoder();

        String passwordHash = sut.encode("secret");
        
        assertThat(sut.matches("secret", passwordHash)).isTrue();
        assertThat(sut.matches("wrong", passwordHash)).isFalse();
    }
}
