package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.PathFinderService;
import nextstep.subway.domain.PathType;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {
    private final PathFinderService pathFinderService;

    public PathResponse getPath(Long sourceId, Long targetId, String type) {
        PathType pathType = PathType.valueOf(type.toUpperCase());
        Path path = pathFinderService.findPath(sourceId, targetId, pathType);
        return PathResponse.of(path, pathType);
    }
}
