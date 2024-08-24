package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.common.exception.SectionNotFoundException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Path {
    private List<Section> sections = new ArrayList<>();
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
        calculateTotalAttribute(stations, allSections);
        this.fare = calculateFare();
    }

    public static Path createPath(List<Station> stations, List<Section> allSections, PathType pathType, int totalWeight) {
        return new Path(stations, allSections, pathType, totalWeight);
    }

    private void calculateTotalAttribute(List<Station> pathStations, List<Section> allSections) {
        for (int i = 0; i < pathStations.size() - 1; i++) {
            Station upStation = pathStations.get(i);
            Station downStation = pathStations.get(i + 1);
            Section section = findSection(upStation, downStation, allSections);

            this.sections.add(section);
            this.distance += findSectionDistance(section);
            this.duration += findSectionDuration(section);
        }
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
        int extraFare = calculateDistanceFare();
        int lineExtraFare = calculateLineExtraFare();
        return baseFare + extraFare + lineExtraFare;
    }

    private int calculateDistanceFare() {
        if (distance <= 10) {
            return 0;
        }
        if (distance <= 50) {
            return ((distance - 10 + 4) / 5) * 100;
        }
        return 800 + ((distance - 50 + 7) / 8) * 100;
    }

    private int calculateLineExtraFare() {
        return sections.stream()
                .map(Section::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    public boolean isValid() {
        return !this.getStations().isEmpty() && this.getTotalWeight() > 0;
    }
}
