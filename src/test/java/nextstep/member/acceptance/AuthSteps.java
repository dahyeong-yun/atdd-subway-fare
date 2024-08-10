package nextstep.member.acceptance;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.application.dto.TokenRequest;

public class AuthSteps {

    public static ExtractableResponse<Response> 로그인_요청(TokenRequest tokenRequest) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/login/token")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(String accessToken) {
        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/members/me")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    public static ExtractableResponse<Response> github_로그인_요청(TokenRequest tokenRequest) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tokenRequest)
            .when().post("/login/github")
            .then().log().all()
            .extract();
    }
}
