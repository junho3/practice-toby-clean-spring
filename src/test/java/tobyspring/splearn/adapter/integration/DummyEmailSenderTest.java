package tobyspring.splearn.adapter.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import tobyspring.splearn.domain.Email;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DummyEmailSender")
class DummyEmailSenderTest {

    @Test
    @StdIo
    @DisplayName("DummyEmailSender 테스트")
    void test1(StdOut stdOut) {
        final DummyEmailSender dummyEmailSender = new DummyEmailSender();

        dummyEmailSender.send(new Email("xxxx@gmail.com"), "text", "text");
        
        assertThat(stdOut.capturedLines()[0]).isEqualTo("Dummy Email Sender: Email[address=xxxx@gmail.com]");
    }
}
