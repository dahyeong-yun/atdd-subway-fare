package nextstep.subway.domain;

import nextstep.common.exception.SectionNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistanceCalculator {
    public int calculateTotalDistance(List<Station> pathStations, List<Section> allSections) {
        int totalDistance = 0;
        for (int i = 0; i < pathStations.size() - 1; i++) {
            Station upStation = pathStations.get(i);
            Station downStation = pathStations.get(i + 1);
            totalDistance += findSectionDistance(upStation, downStation, allSections);
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
}
