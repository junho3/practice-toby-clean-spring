package tobyspring.splearn.adapter.webapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tobyspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

@RestController
@RequiredArgsConstructor
public class MemberApi {

    private final MemberRegister memberRegister;

    /**
     * domain에 위치한 MemberRegisterRequest를 직접 사용하는 것은 의아함
     * 1. web 레이어에서만 필요한 어노테이션이 추가 될 수 있음
     * 2. memberRegister는 web 외에도 다른 곳에서 호출될 수 있는데, MemberRegisterRequest를 직접 사용하면 web과 강결합이 됨
     */
    @PostMapping("/api/v1/members")
    public MemberRegisterResponse register(@Valid @RequestBody MemberRegisterRequest request) {
        final Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }
}
