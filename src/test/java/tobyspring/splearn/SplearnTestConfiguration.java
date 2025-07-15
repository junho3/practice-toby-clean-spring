package tobyspring.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.PasswordEncoder;

@TestConfiguration
public class SplearnTestConfiguration {

    // EmailSender가 개발된게 아니기 때문에 MemberRegister 테스트를 위해 익명클래스로 임시 구현
    @Bean
    public EmailSender emailSender() {
        return (email, subject, body) -> System.out.println("Sending Email: " + email);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}
