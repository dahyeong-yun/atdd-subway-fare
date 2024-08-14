package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.SharedContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationStepDef implements En {
    private final SharedContext sharedContext;
    ExtractableResponse<Response> response;

    public StationStepDef(SharedContext sharedContext) {
        this.sharedContext = sharedContext;
        Given("{string} 역을 생성 하고", this::지하철_역_생성);

        Given("다음과 같은 지하철 역들을 생성하고", (DataTable dataTable) -> {
            List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
            for (Map<String, String> row : rows) {
                String stationName = row.get("역 이름");
                지하철_역_생성(stationName);
            }
        });

        When("지하철역을 생성하면", () -> {
            지하철_역_생성("강남역");
        });

        Then("지하철역이 생성된다", () -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        Then("지하철역 목록 조회 시 생성한 역을 찾을 수 있다", () -> {
            List<String> stationNames =
                    RestAssured.given().log().all()
                            .when().get("/stations")
                            .then().log().all()
                            .extract().jsonPath().getList("name", String.class);
            assertThat(stationNames).containsAnyOf("강남역");
        });
    }

    private void 지하철_역_생성(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);
        response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();

        Long stationId = response.jsonPath().getLong("id");
        sharedContext.addStationId(stationName, stationId);
    }

}
