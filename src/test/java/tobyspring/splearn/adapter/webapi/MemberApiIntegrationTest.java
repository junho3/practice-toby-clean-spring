package tobyspring.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;
import tobyspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyspring.splearn.application.member.provided.MemberRegister;
import tobyspring.splearn.application.member.required.MemberRepository;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.MemberStatus;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static tobyspring.splearn.AssertThatUtils.equalsTo;
import static tobyspring.splearn.AssertThatUtils.notNull;
import static tobyspring.splearn.domain.member.MemberFixture.createMemberRegisterRequest;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
public class MemberApiIntegrationTest {

    private final MockMvcTester mockMvcTester;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final MemberRegister memberRegister;

    @Test
    @DisplayName("[POST] /api/v1/members - 200 OK")
    void test1() throws JsonProcessingException, UnsupportedEncodingException {
        MemberRegisterRequest request = createMemberRegisterRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        MvcTestResult result = mockMvcTester.post().uri("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.memberId", notNull())
                .hasPathSatisfying("$.email", equalsTo(request));

        MemberRegisterResponse response = objectMapper
                .readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);

        Member member = memberRepository.findById(response.memberId()).orElseThrow();

        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("[POST] /api/v1/members - 409 ERROR")
    void test2() throws JsonProcessingException, UnsupportedEncodingException {
        memberRegister.register(createMemberRegisterRequest());

        MemberRegisterRequest request = createMemberRegisterRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        MvcTestResult result = mockMvcTester.post().uri("/api/v1/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .exchange();

        assertThat(result)
                .apply(print())
                .hasStatus(CONFLICT);
    }
}
