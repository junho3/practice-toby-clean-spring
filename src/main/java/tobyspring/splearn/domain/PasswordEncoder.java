package tobyspring.splearn.domain;

/**
 * PasswordEncoder의 기술적인 요소 때문에 required에 위치시켜야하는 의문이 들 수 있음
 * required에 두면 Member(도메인)이 required를 의존하기 때문에 역방향이 됨
 * domain도 application 내부에 있기 때문에 PasswordEncoder는 domain에 위치해도 된다는 내용
 */
public interface PasswordEncoder {

    String encode(String password);
    boolean matches(String password, String passwordHash);
}
