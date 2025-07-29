package tobyspring.splearn.adapter.integration;

import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.domain.shared.Email;

@Fallback // 동일한 Bean 존재하지 않을 때만 사용, 동일한 Bean이 존재할 경우 미사용, @Primary의 반대 개념
@Component
public class DummyEmailSender implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("Dummy Email Sender: " + email);
    }
}
