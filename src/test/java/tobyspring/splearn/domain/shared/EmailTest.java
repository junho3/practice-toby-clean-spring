package tobyspring.splearn.domain.shared;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Email")
class EmailTest {

    @Test
    @DisplayName("동등성 비교")
    void test1() {
        var email1 = new Email("email@github.com");
        var email2 = new Email("email@github.com");

        assertThat(email1).isEqualTo(email2);
    }
}
