package tobyspring.splearn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.regex.Pattern;

/**
 * 원래 @Embeddable은 기본 생성자가 필요함
 * record는 기본 생성자를 만들어주지 않아서 jpa, hibernate 구 버전에서는 규약 위반
 *
 * hibernate 6.x, jpa 3.2 버전부터 record를 지원함
 */
@Embeddable
public record Email(
        @Column(name = "email_address", length = 150, nullable = false)
        String address
) {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Email {
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("이메일 형식이 바르지 않습니다: " + address);
        }
    }
}
