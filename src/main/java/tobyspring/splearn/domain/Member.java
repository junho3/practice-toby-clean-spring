package tobyspring.splearn.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

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
 */
@Getter
@Table(
        name = "MEMBER",
        uniqueConstraints = @UniqueConstraint(name = "UK_MEMBER_EMAIL_ADDRESS", columnNames = "email_address")
)
@Entity
@ToString
@NaturalIdCache // 영속성 컨텍스트에서 @NaturalId가 붙은 필드를 기준으로 캐싱해줌
@NoArgsConstructor(access = PROTECTED)
public class Member extends AbstractEntity {

    @Embedded
    @NaturalId // hibernate에서 제공하는 어노테이션으로 테이블 생성 시 컬럼에 유니크키 설정이 됨
    private Email email;

    @Column(length = 100, nullable = false)
    private String nickname;

    @Column(length = 200, nullable = false)
    private String passwordHash;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public static Member register(MemberRegisterRequest memberRegisterRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(memberRegisterRequest.email());
        member.nickname = requireNonNull(memberRegisterRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(memberRegisterRequest.password()));
        member.status = MemberStatus.PENDING;

        return member;
    }

    public void activate() {
        state(MemberStatus.PENDING == status, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        state(MemberStatus.ACTIVE == status, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
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
