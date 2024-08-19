package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;
import nextstep.common.exception.PathNotFoundException;
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

    private final FareCalculator fareCalculator;
    private final DistanceCalculator distanceCalculator;


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

        if (shortestPath.isNotValidPath()) {
            throw new PathNotFoundException(sourceStation.getId(), targetStation.getId());
        }

        int totalDistance = distanceCalculator.calculateTotalDistance(shortestPath.getPathStations(), allSections);
        int fare = fareCalculator.calculateFare(totalDistance);

        return shortestPath.addFare(fare);
    }
}
