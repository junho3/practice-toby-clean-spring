package tobyspring.splearn.domain.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;
import tobyspring.splearn.domain.AbstractEntity;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class MemberDetail extends AbstractEntity {
    @Embedded
    private Profile profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();

        return memberDetail;
    }

    public void activated() {
        Assert.isTrue(activatedAt == null, "이미 ActivatedAt는 설정되었습니다.");

        this.activatedAt = LocalDateTime.now();
    }

    public void deactivated() {
        Assert.isTrue(deactivatedAt == null, "이미 DeactivatedAt는 설정되었습니다.");

        this.deactivatedAt = LocalDateTime.now();
    }
}
