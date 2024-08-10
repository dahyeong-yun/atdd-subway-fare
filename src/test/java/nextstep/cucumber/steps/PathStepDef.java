package nextstep.cucumber.steps;

import static nextstep.subway.application.DefaultLineCommandService.*;
import static nextstep.subway.domain.model.FareCalculator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.member.acceptance.AuthSteps;
import nextstep.member.acceptance.MemberSteps;
import nextstep.member.application.dto.TokenRequest;
import nextstep.subway.acceptance.TestFixture;
import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.domain.model.PathType;
import nextstep.utils.AcceptanceTest;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = {AcceptanceTest.class})
public class PathStepDef implements En {

    @Autowired
    private AcceptanceContext context;

    public PathStepDef() {

        Given("지하철역들을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> stations = table.asMaps(String.class, String.class);
            for (Map<String, String> station : stations) {
                String stationName = station.get("name");
                ExtractableResponse<Response> response = TestFixture.createStation(stationName);
                Long stationId = response.jsonPath().getLong("id");
                context.store.put(stationName, stationId);
            }
        });

        Given("지하철 노선을 생성 요청하고", (DataTable table) -> {
            List<Map<String, String>> lines = table.asMaps(String.class, String.class);
            for (Map<String, String> line : lines) {
                Long startStationId = context.store.get(line.get("startStation"));
                Long endStationId = context.store.get(line.get("endStation"));

                if (startStationId == null || endStationId == null) {
                    throw new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE);
                }

                LineRequest lineRequest = new LineRequest(
                    line.get("name"),
                    line.get("color"),
                    startStationId,
                    endStationId,
                    Integer.parseInt(line.get("distance")),
                    Integer.parseInt(line.get("duration")),
                    Integer.parseInt(line.get("additionalFare"))
                );
                ExtractableResponse<Response> response = TestFixture.createLine(lineRequest);
                Long lineId = response.jsonPath().getLong("id");
                context.store.put(line.get("name"), lineId);
            }
        });

        Given("구간을 추가 요청하고", (DataTable table) -> {
            List<Map<String, String>> sections = table.asMaps(String.class, String.class);
            for (Map<String, String> section : sections) {
                Long lineId = context.store.get(section.get("line"));

                if (lineId == null) {
                    throw new IllegalArgumentException(LINE_NOT_FOUND_MESSAGE);
                }

                Long upStationId = context.store.get(section.get("upStation"));
                Long downStationId = context.store.get(section.get("downStation"));

                if (upStationId == null || downStationId == null) {
                    throw new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE);
                }

                int distance = Integer.parseInt(section.get("distance"));
                int duration = Integer.parseInt(section.get("duration"));

                TestFixture.addSection(lineId, upStationId, downStationId, distance, duration);
            }
        });

        Given("회원가입을 요청하고", (DataTable table) -> {
            List<Map<String, String>> members = table.asMaps(String.class, String.class);
            for (Map<String, String> member : members) {
                String email = member.get("email");
                String password = member.get("password");
                Integer age = Integer.parseInt(member.get("age"));
                MemberSteps.회원_생성_요청(email, password, age);
            }
        });


        When("로그인 요청을 하고", (DataTable table) -> {
            List<Map<String, String>> users = table.asMaps(String.class, String.class);
            for (Map<String, String> user : users) {
                TokenRequest tokenRequest = TokenRequest.ofEmailAndPassword(user.get("email"), user.get("password"));
                ExtractableResponse<Response> response = AuthSteps.로그인_요청(tokenRequest);

                String accessToken = response.jsonPath().getString("accessToken");
                context.tokenStore.put(user.get("email"), accessToken);
            }
        });

        When("{string}과 {string}의 경로를 조회하면", (String startStation, String endStation) -> {
            Long startStationId = context.store.get(startStation);
            Long endStationId = context.store.get(endStation);

            if (startStationId == null || endStationId == null) {
                throw new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE);
            }

            ExtractableResponse<Response> response = TestFixture.getPaths(startStationId, endStationId,
                PathType.DISTANCE);
            context.response = response;
        });

        When("로그인 사용자 {string}가 {string}과 {string}의 경로를 거리 기준으로 조회하면", (String email, String startStation, String endStation) -> {
            Long startStationId = context.store.get(startStation);
            Long endStationId = context.store.get(endStation);

            if (startStationId == null || endStationId == null) {
                throw new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE);
            }

            String accessToken = context.tokenStore.get(email);
            ExtractableResponse<Response> response = TestFixture.getPaths(startStationId, endStationId, PathType.DISTANCE, accessToken);
            context.response = response;
        });

        When("출발역이 null인 경로를 조회하면", () -> {
            ExtractableResponse<Response> response = TestFixture.getPaths(
                null,
                context.store.get("강남역"),
                PathType.DISTANCE
            );
            context.response = response;
        });

        When("출발역이 {string}인 경로를 조회하면", (String source) -> {
            Long sourceId = source.equals("null") ? null : Long.parseLong(source);
            ExtractableResponse<Response> response = TestFixture.getPaths(
                sourceId,
                context.store.get("강남역"),
                PathType.DISTANCE
            );
            context.response = response;
        });

        When("도착역이 null인 경로를 조회하면", () -> {
            ExtractableResponse<Response> response = TestFixture.getPaths(
                context.store.get("강남역"),
                null,
                PathType.DISTANCE
            );
            context.response = response;
        });

        When("도착역이 {string}인 경로를 조회하면", (String target) -> {
            Long targetId = target.equals("null") ? null : Long.parseLong(target);
            ExtractableResponse<Response> response = TestFixture.getPaths(
                context.store.get("강남역"),
                targetId,
                PathType.DISTANCE
            );
            context.response = response;
        });

        When("출발역에서 도착역까지의 최소 거리 기준으로 경로 조회를 요청하면", (DataTable table) -> {
            List<Map<String, String>> params = table.asMaps(String.class, String.class);
            for (Map<String, String> param : params) {
                Long sourceId = context.store.get(param.get("source"));
                Long targetId = context.store.get(param.get("target"));

                if (sourceId == null || targetId == null) {
                    throw new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE);
                }

                ExtractableResponse<Response> response = TestFixture.getPaths(sourceId, targetId, PathType.of(param.get("type")));
                context.response = response;
            }
        });

        When("출발역에서 도착역까지의 최소 시간 기준으로 경로 조회를 요청하면", (DataTable table) -> {
            List<Map<String, String>> params = table.asMaps(String.class, String.class);
            for (Map<String, String> param : params) {
                Long sourceId = context.store.get(param.get("source"));
                Long targetId = context.store.get(param.get("target"));

                if (sourceId == null || targetId == null) {
                    throw new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE);
                }

                ExtractableResponse<Response> response = TestFixture.getPaths(sourceId, targetId, PathType.of(param.get("type")));
                context.response = response;
            }
        });

        Then("경로가 정상적으로 조회된다", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.OK.value());
        });

        Then("역이 하나만 조회된다", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(context.response.jsonPath().getList("stations.id", Long.class)).hasSize(1);
        });

        Then("최소 거리 기준으로 경로가 정상적으로 조회된다", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(51);
        });

        Then("최소 시간 기준으로 경로가 정상적으로 조회된다", () -> {
            assertThat(context.response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(102);
        });

        Then("예외가 발생한다", () -> {
            int statusCode = context.response.statusCode();
            assertThat(statusCode).isBetween(400, 599);
        });

        And("총 거리와 소요 시간을 함께 응답한다", () -> {
            assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(51);
            assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(102);
        });

        Then("신논현역과 양재역의 총 거리와 소요 시간을 함께 응답한다", () -> {
            assertThat(context.response.jsonPath().getInt("distance")).isEqualTo(71);
            assertThat(context.response.jsonPath().getInt("duration")).isEqualTo(122);
        });

        And("지하철 이용 요금도 함께 응답한다", () -> {
            assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(2650);
        });

        Then("유아 할인 요금이 적용된 경로 요금을 응답한다", () -> {
            assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(0);
        });

        Then("어린이 할인 요금이 적용된 경로 요금을 응답한다", () -> {
            assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(1150);
        });

        Then("청소년 할인 요금이 적용된 경로 요금을 응답한다", () -> {
            assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(1840);
        });

        Then("추가 요금이 포함된 기본 요금을 응답한다", () -> {
            assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(2650);
        });

        Then("가장 높은 추가 요금이 포함된 요금을 응답한다", () -> {
            assertThat(context.response.jsonPath().getInt("fare")).isEqualTo(3250);
        });
    }
}