package tobyspring.splearn.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import tobyspring.splearn.domain.AbstractEntity;
import tobyspring.splearn.domain.shared.Email;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.state;

/**
 * 코드에 어노테이션이 있다고 기술에 의존적(종속적)으로 되었는가?
 * JPA가 없으면 사용할 수 없는 코드인가?
 *
 * 어노테이션의 의미는 주석
 * 어노테이션 때문에 특별한 동작을 수행하는 프레임워크가 아니라면 기술에 의존인건 아님
 * 순수한 도메인으로 만들었을 때와 코드가 달라져야하는데, 어노테이션만으로는 차이가 없음
 *
 * XML 방식을 사용해서 JPA 관련 어노테이션과 설정을 걷어낼 수 있음
 */
@Entity
@Getter
@ToString(callSuper = true, exclude = "detail") // AbstractEntity 내용까지 포함하여 출력, detail은 제외
@NaturalIdCache // 영속성 컨텍스트에서 @NaturalId가 붙은 필드를 기준으로 캐싱해줌
@NoArgsConstructor(access = PROTECTED)
public class Member extends AbstractEntity {
    @NaturalId // hibernate에서 제공하는 어노테이션으로 테이블 생성 시 컬럼에 유니크키 설정이 됨
    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MemberDetail detail;

    public static Member register(MemberRegisterRequest memberRegisterRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(memberRegisterRequest.email());
        member.nickname = requireNonNull(memberRegisterRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(memberRegisterRequest.password()));
        member.status = MemberStatus.PENDING;
        member.detail = MemberDetail.create();

        return member;
    }

    public void activate() {
        state(MemberStatus.PENDING == status, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
        this.detail.activated();
    }

    public void deactivate() {
        state(MemberStatus.ACTIVE == status, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
        this.detail.deactivated();
    }

    public void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.nickname = Objects.requireNonNull(updateRequest.nickname());

        this.detail.updateInfo(updateRequest);
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return MemberStatus.ACTIVE == this.status;
    }
}
