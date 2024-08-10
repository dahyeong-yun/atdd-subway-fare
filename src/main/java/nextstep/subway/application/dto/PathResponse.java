package nextstep.subway.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;

import nextstep.subway.domain.model.Path;
import nextstep.subway.domain.model.Station;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PathResponse {
    private List<StationResponse> stations;
    private Integer distance;
    private Integer duration;
    private Integer fare;

    public PathResponse(List<Station> stations, int distance, int duration, int fare) {
        this.stations = stations.stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getFare() {
        return fare;
    }

    public static PathResponse of(Path path) {
        return new PathResponse(
            path.getStations(),
            path.getDistance(),
            path.getDuration(),
            path.getFare()
        );
    }
}

