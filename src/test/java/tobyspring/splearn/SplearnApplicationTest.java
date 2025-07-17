package tobyspring.splearn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

@DisplayName("SplearnApplication")
class SplearnApplicationTest {

    @Test
    @DisplayName("run()")
    void test1() {
        MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class);

        SplearnApplication.main(new String[0]);

        mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
    }
}
