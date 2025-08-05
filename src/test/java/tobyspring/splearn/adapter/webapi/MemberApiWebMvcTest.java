package tobyspring.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tobyspring.splearn.domain.member.MemberFixture.createMember;
import static tobyspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;

@WebMvcTest(MemberApi.class)
@DisplayName("MemberApiWebMvc")
@RequiredArgsConstructor
class MemberApiWebMvcTest {
    @MockitoBean
    private MemberRegister memberRegister;

    private final MockMvcTester mockMvcTester;
    private final ObjectMapper objectMapper;

    @Test
    @DisplayName("[POST] /api/v1/members - 200 OK")
    void test1() throws JsonProcessingException {
        Member member = createMember(1L);
        when(memberRegister.register(any())).thenReturn(member);

        MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest();
        String requestBody = objectMapper.writeValueAsString(memberRegisterRequest);

        assertThat(mockMvcTester.post().uri("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.memberId")
                .asNumber()
                .isEqualTo(1);

        verify(memberRegister).register(memberRegisterRequest);
    }

    @Test
    @DisplayName("[POST] /api/v1/members - 400 ERROR")
    void test2() throws JsonProcessingException {
        MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest("invalid email");
        String requestBody = objectMapper.writeValueAsString(memberRegisterRequest);

        assertThat(mockMvcTester.post().uri("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .hasStatus4xxClientError();
    }
}
