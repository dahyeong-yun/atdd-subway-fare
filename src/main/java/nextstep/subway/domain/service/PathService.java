package nextstep.subway.domain.service;

import nextstep.member.domain.LoginMember;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.model.PathType;

public interface PathService {
    boolean pathExists(Long sourceId, Long targetId);

    PathResponse findPath(Long sourceId, Long targetId, PathType pathType);

    PathResponse findPath(Long sourceId, Long targetId, PathType pathType, LoginMember loginMember);
}
