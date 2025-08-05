package tobyspring.splearn;

import org.assertj.core.api.AssertProvider;
import org.springframework.test.json.JsonPathValueAssert;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertThatUtils {

    public static Consumer<AssertProvider<JsonPathValueAssert>> notNull() {
        return it -> assertThat(it).isNotNull();
    }

    public static Consumer<AssertProvider<JsonPathValueAssert>> equalsTo(MemberRegisterRequest request) {
        return it -> assertThat(it).isEqualTo(request.email());
    }
}
