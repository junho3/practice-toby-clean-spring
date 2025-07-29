package tobyspring.splearn.domain.member;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    private String profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;
}
