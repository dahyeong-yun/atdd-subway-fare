package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PathResult {
    private final List<Station> pathStations;
    private final PathType pathType;
    private final int totalPathWeight;
    private int totalFare;

    public PathResult addFare(int fare) {
        return new PathResult(pathStations, pathType, totalPathWeight, fare);
    }

    public boolean isNotValidPath() {
        return this.getPathStations().isEmpty() || this.getTotalPathWeight() <= 0;
    }
}
