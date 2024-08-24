package nextstep.subway.domain;

public interface PathFinderService {
    Path findPath(Long sourceId, Long targetId, PathType pathType);
}
