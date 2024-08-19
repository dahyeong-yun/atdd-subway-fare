package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.common.exception.PathNotFoundException;
import nextstep.subway.domain.PathFinderService;
import nextstep.subway.domain.PathResult;
import nextstep.subway.domain.PathType;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {
    private final PathFinderService pathFinderService;

    public PathResponse getPath(Long sourceId, Long targetId, String type) {
        if (!pathFinderService.isValidPath(sourceId, targetId)) {
            throw new PathNotFoundException(sourceId, targetId);
        }

        PathType pathType = PathType.valueOf(type.toUpperCase());
        PathResult pathResult = pathFinderService.findPath(sourceId, targetId, pathType);


        return PathResponse.of(pathResult, pathType);
    }
}
