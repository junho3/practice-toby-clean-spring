package tobyspring.splearn.application.member.provided;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import tobyspring.splearn.application.member.MemberModifyService;
import tobyspring.splearn.application.member.required.EmailSender;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.shared.Email;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("MemberRegister")
class MemberRegisterManualTest {

    @Test
    @DisplayName("stub 테스트 - 값 검증")
    void test1() {
        final MemberRegister memberRegister = new MemberModifyService(
                new MemberFinderMock(),
                new MemberRepositoryStub(),
                new EmailSenderStub(),
                MemberFixture.createPasswordEncoder()
        );

        final Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("mock 테스트 - 행위 검증")
    void test2() {
        final EmailSenderMock emailSenderMock = new EmailSenderMock();
        final MemberRegister memberRegister = new MemberModifyService(
                new MemberFinderMock(),
                new MemberRepositoryStub(),
                emailSenderMock,
                MemberFixture.createPasswordEncoder()
        );

        final Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        assertThat(emailSenderMock.tos).hasSize(1);
        assertThat(emailSenderMock.tos.getFirst()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("mockito 테스트 - 행위 검증")
    void test3() {
        final EmailSenderMock emailSenderMock = Mockito.mock(EmailSenderMock.class);
        final MemberRegister memberRegister = new MemberModifyService(
                new MemberFinderMock(),
                new MemberRepositoryStub(),
                emailSenderMock,
                MemberFixture.createPasswordEncoder()
        );

        final Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

        verify(emailSenderMock, times(1)).send(eq(member.getEmail()), any(), any());
    }

    // stub
    static class MemberRepositoryStub implements MemberRepository {
        @Override
        public Member save(Member member) {
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        }

        @Override
        public Optional<Member> findByEmail(Email email) {
            return Optional.empty();
        }

        @Override
        public Optional<Member> findById(Long memberId) {
            return Optional.empty();
        }
    }

    static class EmailSenderStub implements EmailSender {
        @Override
        public void send(Email email, String subject, String body) {
            //
        }
    }

    // mock
    static class EmailSenderMock implements EmailSender {
        final List<Email> tos = new ArrayList<>();

        @Override
        public void send(Email email, String subject, String body) {
            tos.add(email);
        }
    }

    static class MemberFinderMock implements MemberFinder {

        @Override
        public Member find(Long memberId) {
            return null;
        }
    }
}
