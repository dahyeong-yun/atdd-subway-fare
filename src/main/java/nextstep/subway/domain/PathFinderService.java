package nextstep.subway.domain;

public interface PathFinderService {
    PathResult findPath(Long sourceId, Long targetId, PathType pathType);
}
