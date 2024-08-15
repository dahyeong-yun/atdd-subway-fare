package nextstep.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.cucumber.SharedContext;
import nextstep.subway.presentation.SectionRequest;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

public class SectionStepDef implements En {
    private final SharedContext sharedContext;
    ExtractableResponse<Response> response;

    public SectionStepDef(SharedContext sharedContext) {
        this.sharedContext = sharedContext;

        Given("다음과 같은 지하철 구간들을 생성하고", (DataTable dataTable) -> {
            List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
            for (Map<String, String> row : rows) {
                String lineName = row.get("노선명");
                String upStation = row.get("상행역");
                String downStation = row.get("하행역");
                int distance = Integer.parseInt(row.get("거리"));

                Long lineId = sharedContext.getLineId(lineName);
                Long upStationId = sharedContext.getStationId(upStation);
                Long downStationId = sharedContext.getStationId(downStation);

                SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);
                지하철_구간_생성(lineId, sectionRequest);
            }
        });
    }

    private void 지하철_구간_생성(Long lineId, SectionRequest sectionRequest) {
        response = RestAssured.given().log().all()
                .body(sectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }
}
