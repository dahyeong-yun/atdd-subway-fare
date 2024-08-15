package nextstep.subway.application;

import lombok.Builder;
import lombok.Getter;
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

    public static PathResponse of(List<Station> stations, PathType pathType, int distance) {
        List<StationResponse> stationResponses = stations.stream().map(StationResponse::of).collect(Collectors.toList());
        return PathResponse.builder()
                .stations(stationResponses)
                .pathType(pathType)
                .amount(distance)
                .build();
    }
}
