package tobyspring.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.*;

@Service
@RequiredArgsConstructor // Refactor > Delombok 으로 lombok이 어떤 코드를 만드는지 알 수 있음
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(final MemberRegisterRequest memberRegisterRequest) {
        checkDuplicateEmail(memberRegisterRequest);

        final Member member = Member.register(memberRegisterRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.", "아래 링크를 클릭해서 등록을 완료해주세요.");
    }

    private void checkDuplicateEmail(final MemberRegisterRequest memberRegisterRequest) {
        if (memberRepository.findByEmail(new Email(memberRegisterRequest.email())).isPresent()) {
            throw new DuplicateEmailException("이미 사용 중인 이메일 입니다. : " + memberRegisterRequest.email());
        }
    }
}
