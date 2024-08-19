package nextstep.subway.application;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.PathResult;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class PathResponse {
    private List<StationResponse> stations;
    private PathType pathType;
    private int amount;
    private int fare;

    public static PathResponse of(PathResult pathResult, PathType pathType) {
        List<Station> stations = pathResult.getPathStations();
        List<StationResponse> stationResponses = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        return PathResponse.builder()
                .stations(stationResponses)
                .pathType(pathType)
                .amount(pathResult.getTotalPathWeight())
                .fare(pathResult.getTotalFare())
                .build();
    }
}
