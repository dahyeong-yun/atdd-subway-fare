package nextstep.subway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class PathResult {
    private final List<Station> pathStations;
    private final PathType pathType;
    private final int totalPathWeight;
}
