package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.common.exception.SectionNotFoundException;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Path {
    private final List<Station> stations;
    private final PathType pathType;
    private final int totalWeight;
    private int fare;
    private int distance;
    private int duration;

    private Path(List<Station> stations, List<Section> allSections, PathType pathType, int totalWeight) {
        this.stations = stations;
        this.pathType = pathType;
        this.totalWeight = totalWeight;
        this.duration = calculateTotalDuration(stations, allSections);
        this.distance = calculateTotalDistance(stations, allSections);
        this.fare = calculateFare();

    }

    public static Path createPath(List<Station> stations, List<Section> allSections, PathType pathType, int totalWeight) {
        return new Path(stations, allSections, pathType, totalWeight);
    }

    private int calculateTotalDistance(List<Station> pathStations, List<Section> allSections) {
        int totalDistance = 0;
        for (int i = 0; i < pathStations.size() - 1; i++) {
            Station upStation = pathStations.get(i);
            Station downStation = pathStations.get(i + 1);
            totalDistance += findSectionDistance(upStation, downStation, allSections);
        }
        return totalDistance;
    }

    private int calculateTotalDuration(List<Station> pathStations, List<Section> allSections) {
        int totalDistance = 0;
        for (int i = 0; i < pathStations.size() - 1; i++) {
            Station upStation = pathStations.get(i);
            Station downStation = pathStations.get(i + 1);
            totalDistance += findSectionDuration(upStation, downStation, allSections);
        }
        return totalDistance;
    }

    private int findSectionDistance(Station upStation, Station downStation, List<Section> allSections) {
        return allSections.stream()
                .filter(section -> (section.containsStations(upStation, downStation)))
                .findFirst()
                .map(section -> section.getSectionDistance().getDistance())
                .orElseThrow(() -> new SectionNotFoundException(upStation, downStation));
    }

    private int findSectionDuration(Station upStation, Station downStation, List<Section> allSections) {
        return allSections.stream()
                .filter(section -> (section.containsStations(upStation, downStation)))
                .findFirst()
                .map(Section::getSectionDuration)
                .orElseThrow(() -> new SectionNotFoundException(upStation, downStation));
    }

    private int calculateFare() {
        int distance = getDistance();
        int baseFare = 1250;
        int extraFare = 0;

        if (distance > 10) {
            extraFare += ((distance - 10) / 5) * 100;
        }
        if (distance > 50) {
            extraFare += ((distance - 50) / 8) * 100;
        }

        this.fare = baseFare + extraFare;
        return this.fare;
    }

    public boolean isValid() {
        return !this.getStations().isEmpty() && this.getTotalWeight() > 0;
    }
}
