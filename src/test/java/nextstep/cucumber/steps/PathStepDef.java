package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.SharedContext;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PathStepDef implements En {
    private final SharedContext sharedContext;
    ExtractableResponse<Response> response;

    public PathStepDef(SharedContext sharedContext) {
        this.sharedContext = sharedContext;

        When("{string}에서 {string}까지의 {string} 경로를 조회 하면", (String source, String target, String pathType) -> {
            Long sourceStationId = sharedContext.getStationId(source);
            Long targetStationId = sharedContext.getStationId(target);
            경로_찾기(sourceStationId, targetStationId, pathType);
        });

        Then("다음과 같은 경로와 거리 및 요금을 응답받는다", (DataTable dataTable) -> {
            검증_경로_및_값_확인(dataTable, "distance");
        });

        Then("다음과 같은 경로와 시간 및 요금을 응답받는다", (DataTable dataTable) -> {
            검증_경로_및_값_확인(dataTable, "duration");
        });
    }

    private void 경로_찾기(Long sourceStationId, Long targetStationId, String pathType) {
        String type = pathType.equals("최소시간") ? "DURATION" : "DISTANCE";
        response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={source}&target={target}&type={type}", sourceStationId, targetStationId, type)
                .then().log().all()
                .extract();
    }

    private void 검증_경로_및_값_확인(DataTable dataTable, String valueType) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String expectedPath = row.get("경로");
            int expectedDistance = valueType.equals("distance") ? Integer.parseInt(row.get("거리")) : 0;
            int expectedDuration = valueType.equals("duration") ? Integer.parseInt(row.get("시간")) : 0;
            int expectedFare = Integer.parseInt(row.get("요금"));

            List<String> actualStations = response.jsonPath().getList("stations.name");
            int actualAmount = response.jsonPath().getInt("amount");
            int actualFare = response.jsonPath().getInt("fare");

            String actualPath = String.join(" - ", actualStations);

            assertThat(actualPath).isEqualTo(expectedPath);

            if (valueType.equals("distance")) {
                assertThat(actualAmount).isEqualTo(expectedDistance);
            } else {
                assertThat(actualAmount).isEqualTo(expectedDuration);
            }

            assertThat(actualFare).isEqualTo(expectedFare);
        }
    }
}
