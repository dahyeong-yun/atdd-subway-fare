package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.common.exception.SectionNotFoundException;

import java.util.List;
import java.util.function.Function;

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
        this.distance = calculateTotalAttribute(stations, allSections, this::findSectionDistance);
        this.duration = calculateTotalAttribute(stations, allSections, this::findSectionDuration);
        this.fare = calculateFare();
    }

    public static Path createPath(List<Station> stations, List<Section> allSections, PathType pathType, int totalWeight) {
        return new Path(stations, allSections, pathType, totalWeight);
    }

    private int calculateTotalAttribute(List<Station> pathStations, List<Section> allSections,
                                        Function<Section, Integer> attributeExtractor) {
        int total = 0;
        for (int i = 0; i < pathStations.size() - 1; i++) {
            Station upStation = pathStations.get(i);
            Station downStation = pathStations.get(i + 1);
            Section section = findSection(upStation, downStation, allSections);
            total += attributeExtractor.apply(section);
        }
        return total;
    }

    private Section findSection(Station upStation, Station downStation, List<Section> allSections) {
        return allSections.stream()
                .filter(section -> section.containsStations(upStation, downStation))
                .findFirst()
                .orElseThrow(() -> new SectionNotFoundException(upStation, downStation));
    }

    private int findSectionDistance(Section section) {
        return section.getSectionDistance().getDistance();
    }

    private int findSectionDuration(Section section) {
        return section.getSectionDuration();
    }

    private int calculateFare() {
        int baseFare = 1250;
        int extraFare = 0;

        if (distance > 10 && distance <= 50) {
            extraFare += ((distance - 10 + 4) / 5) * 100;
        } else if (distance > 50) {
            extraFare += 8 * 100;  // 10km ~ 50km 구간의 요금
            extraFare += ((distance - 50 + 7) / 8) * 100;  // 50km 초과 구간의 요금
        }

        return baseFare + extraFare;
    }

    public boolean isValid() {
        return !this.getStations().isEmpty() && this.getTotalWeight() > 0;
    }
}
