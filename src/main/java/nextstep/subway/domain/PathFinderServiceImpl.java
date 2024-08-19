package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;
import nextstep.common.exception.StationNotFoundException;
import nextstep.subway.infrastructure.SectionRepository;
import nextstep.subway.infrastructure.StationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class PathFinderServiceImpl implements PathFinderService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    private static final int BASE_FARE = 1250;
    private static final int BASE_DISTANCE = 10; // 10km in meters
    private static final int MEDIUM_DISTANCE = 50; // 50km in meters

    @Override
    public PathResult findPath(Long sourceId, Long targetId, PathType pathType) {
        List<Section> allSections = sectionRepository.findAll();
        List<Station> allStations = stationRepository.findAll();

        Station sourceStation = stationRepository.findById(sourceId)
                .orElseThrow(() -> new StationNotFoundException(sourceId));
        Station targetStation = stationRepository.findById(targetId)
                .orElseThrow(() -> new StationNotFoundException(targetId));

        PathFinder pathFinder = PathFinderFactory.createPathFinder(allSections, allStations, pathType);
        PathResult shortestPath = pathFinder.getShortestPath(sourceStation, targetStation, pathType);

        int totalDistance = calculateTotalDistance(shortestPath.getPathStations(), allSections);
        int fare = calculateFare(totalDistance);

        return shortestPath.addFare(fare);
    }

    private int calculateTotalDistance(List<Station> pathStations, List<Section> allSections) {
        int totalDistance = 0;
        for (int i = 0; i < pathStations.size() - 1; i++) {
            Station currentStation = pathStations.get(i);
            Station nextStation = pathStations.get(i + 1);
            totalDistance += findSectionDistance(currentStation, nextStation, allSections);
        }
        return totalDistance;
    }

    private int findSectionDistance(Station station1, Station station2, List<Section> allSections) {
        return allSections.stream()
                .filter(section -> (section.getUpStation().equals(station1) && section.getDownStation().equals(station2)) ||
                        (section.getUpStation().equals(station2) && section.getDownStation().equals(station1)))
                .findFirst()
                .map(section -> section.getSectionDistance().getDistance())
                .orElseThrow(() -> new RuntimeException("Section not found between stations")); //TODO custom throw
    }

    private int calculateFare(int totalDistance) {
        int fare = BASE_FARE;

        if (totalDistance <= BASE_DISTANCE) {
            return fare;
        }

        if (totalDistance <= MEDIUM_DISTANCE) {
            int extraDistance = totalDistance - BASE_DISTANCE;
            fare += (extraDistance / 5) * 100;
        } else {
            int mediumExtraDistance = MEDIUM_DISTANCE - BASE_DISTANCE;
            fare += (mediumExtraDistance / 5) * 100;

            int longExtraDistance = totalDistance - MEDIUM_DISTANCE;
            fare += (longExtraDistance / 8) * 100;
        }

        return fare;
    }

    @Override
    public boolean isValidPath(Long sourceId, Long targetId) {
        try {
            PathResult pathResult = findPath(sourceId, targetId, PathType.DISTANCE);
            return !pathResult.getPathStations().isEmpty() && pathResult.getTotalPathWeight() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
