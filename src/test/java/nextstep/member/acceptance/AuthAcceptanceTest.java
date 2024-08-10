package nextstep.member.acceptance;

import static nextstep.member.acceptance.AuthSteps.*;
import static nextstep.member.application.dto.GithubResponses.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.utils.AcceptanceTest;

class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        // given
        Member member = new Member(사용자1.getEmail(), DEFAULT_PASSWORD, DEFAULT_AGE);
        memberRepository.save(member);
        TokenRequest tokenRequest = TokenRequest.ofEmailAndPassword(member.getEmail(), member.getPassword());

        // when
        ExtractableResponse<Response> 로그인응답 = 로그인_요청(tokenRequest);

        // then
        String accessToken = 로그인응답.jsonPath().getString("accessToken");
        assertThat(accessToken).isNotBlank();

        // when
        ExtractableResponse<Response> 회원정보응답 = 회원_정보_조회_요청(accessToken);

        // then
        assertThat(회원정보응답.jsonPath().getString("email")).isEqualTo(사용자1.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 토큰을 생성하려고 하면 오류가 발생한다")
    void createToken_MemberNotFound() {
        // given
        TokenRequest tokenRequest = TokenRequest.ofEmailAndPassword(PROFILE_없는_사용자.getEmail(), DEFAULT_PASSWORD);

        // when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("틀린 비밀번호로 토큰 생성을 요청하면 오류가 발생한다")
    void createToken_InvalidPassword() {
        // given
        Member member = new Member(사용자2.getEmail(), DEFAULT_PASSWORD, DEFAULT_AGE);
        memberRepository.save(member);
        TokenRequest tokenRequest = TokenRequest.ofEmailAndPassword(member.getEmail(), WRONG_PASSWORD);

        // when
        ExtractableResponse<Response> response = 로그인_요청(tokenRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Github 로그인을 통해 토큰을 발급받는다.")
    void githubAuth() {
        // given
        Member member = new Member(사용자3.getEmail(), DEFAULT_PASSWORD, DEFAULT_AGE);
        memberRepository.save(member);
        TokenRequest tokenRequest = TokenRequest.ofCode(사용자3.getCode());

        // when
        ExtractableResponse<Response> response = github_로그인_요청(tokenRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @Test
    @DisplayName("github를 통해 토큰을 생성하려고 할 때 존재하지 않는 회원에 대해 회원이 가입되고 토큰이 생성된다")
    void createTokenFromGithub_NewMember() {
        // given
        String code = PROFILE_없는_사용자.getCode();
        String email = PROFILE_없는_사용자.getEmail();

        TokenRequest tokenRequest = TokenRequest.ofCode(code);

        // when
        ExtractableResponse<Response> response = github_로그인_요청(tokenRequest);

        // then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();

        // then
        Member member = memberRepository.findByEmail(email).orElseThrow();
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("잘못된 코드 값으로 Github를 통해 토큰을 생성하려고 할 때 오류가 발생한다")
    void createTokenFromGithub_InvalidAccessToken() {
        // given
        TokenRequest tokenRequest = TokenRequest.ofCode(CODE_없는_사용자.getCode());

        // when
        ExtractableResponse<Response> response = github_로그인_요청(tokenRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}