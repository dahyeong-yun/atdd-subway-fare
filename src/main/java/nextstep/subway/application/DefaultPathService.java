package nextstep.subway.application;

import static nextstep.subway.application.DefaultLineCommandService.*;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.model.AgeGroup;
import nextstep.subway.domain.model.Line;
import nextstep.subway.domain.model.Path;
import nextstep.subway.domain.model.PathType;
import nextstep.subway.domain.model.Station;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.repository.StationRepository;
import nextstep.subway.domain.service.PathService;

@Service
public class DefaultPathService implements PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final MemberService memberService;

    public DefaultPathService(
        StationRepository stationRepository,
        LineRepository lineRepository,
        MemberService memberService
    ) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.memberService = memberService;
    }

    @Override
    public PathResponse findPath(Long sourceId, Long targetId, PathType pathType) {
        return findPath(sourceId, targetId, pathType, null);
    }

    @Override
    public PathResponse findPath(Long sourceId, Long targetId, PathType pathType, LoginMember loginMember) {
        Station source = findStationOrElseThrow(sourceId);
        Station target = findStationOrElseThrow(targetId);
        List<Line> lines = lineRepository.findAll();

        Path path = Path.of(lines, source, target, pathType, getAgeGroup(loginMember));
        return PathResponse.of(path);
    }

    private AgeGroup getAgeGroup(LoginMember loginMember) {
        if (loginMember == null) {
            return AgeGroup.ADULT;
        }

        Member member = memberService.findMemberByEmailOrNull(loginMember.getEmail());
        return AgeGroup.from(member);
    }

    @Override
    public boolean pathExists(Long sourceId, Long targetId) {
        Station source = findStationOrElseNull(sourceId);
        Station target = findStationOrElseNull(targetId);

        if (source == null || target == null) {
            return false;
        }

        List<Line> lines = lineRepository.findAll();

        try {
            return (Path.of(lines, source, target) != null);
        } catch (Exception e) {
            return false;
        }
    }

    private Station findStationOrElseThrow(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE));
    }

    private Station findStationOrElseNull(Long stationId) {
        return stationRepository.findById(stationId).orElse(null);
    }
}