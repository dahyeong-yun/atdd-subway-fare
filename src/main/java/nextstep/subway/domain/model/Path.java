package nextstep.subway.domain.model;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.application.DefaultPathFinder;
import nextstep.subway.domain.service.PathFinder;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final int duration;
    private final int fare;

    public Path(List<Station> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public int getFare() {
        return fare;
    }

    public static Path of(List<Line> lines, Station source, Station target) {
        return of(lines, source, target, PathType.DISTANCE, AgeGroup.ADULT);
    }


    public static Path of(List<Line> lines, Station source, Station target, PathType pathType) {
        return of(lines, source, target, pathType, AgeGroup.ADULT);
    }

    public static Path of(List<Line> lines, Station source, Station target, PathType pathType, AgeGroup ageGroup) {
        PathFinder pathFinder = new DefaultPathFinder(lines);
        return pathFinder.findPath(lines, source, target, pathType, ageGroup);
    }
}
