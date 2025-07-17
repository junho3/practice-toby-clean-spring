package tobyspring.splearn.application;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import tobyspring.splearn.application.provided.MemberRegister;
import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.application.required.MemberRepository;
import tobyspring.splearn.domain.*;

@Service
@Validated
@Transactional
@RequiredArgsConstructor // Refactor > Delombok 으로 lombok이 어떤 코드를 만드는지 알 수 있음
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(@Valid final MemberRegisterRequest memberRegisterRequest) {
        checkDuplicateEmail(memberRegisterRequest);

        final Member member = Member.register(memberRegisterRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(final Long memberId) {
        // 람다를 사용하지 않으면 성공 케이스에서도 익셉션 메시지를 만드는 비용이 발생함
        // 람다를 사용하면 익셉션이 발생했을 때만 메시지를 만들기 때문에 비용이 적음
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id: " + memberId));

        member.activate();

        // 도메인 이벤트 발행과 Auditing을 위해 .save()를 명시적으로 선언
        return memberRepository.save(member);
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
