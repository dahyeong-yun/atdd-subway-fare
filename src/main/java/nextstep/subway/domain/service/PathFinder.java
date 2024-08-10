package nextstep.subway.domain.service;

import java.util.List;

import nextstep.subway.domain.model.AgeGroup;
import nextstep.subway.domain.model.Line;
import nextstep.subway.domain.model.Path;
import nextstep.subway.domain.model.PathType;
import nextstep.subway.domain.model.Station;

public interface PathFinder {
    default Path findPath(List<Line> lines, Station source, Station target) {
        return findPath(lines, source, target, PathType.DISTANCE, AgeGroup.ADULT);
    }

    default Path findPath(List<Line> lines, Station source, Station target, PathType pathType) {
        return findPath(lines, source, target, pathType, AgeGroup.ADULT);
    }

    Path findPath(List<Line> lines, Station source, Station target, PathType pathType, AgeGroup ageGroup);
}
