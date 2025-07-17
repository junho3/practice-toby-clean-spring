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
        // static 테스트 할 때 try 블록으로 반드시 감싸줘야함
        // 감싸지 않으면 간헐적인 테스트 실패 발생
        try(MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {

            SplearnApplication.main(new String[0]);

            mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
        }
    }
}
