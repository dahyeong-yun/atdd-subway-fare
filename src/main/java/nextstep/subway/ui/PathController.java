package nextstep.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nextstep.member.domain.LoginMember;
import nextstep.member.ui.AuthenticationPrincipal;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.domain.model.PathType;
import nextstep.subway.domain.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(
        @RequestParam Long source,
        @RequestParam Long target,
        @RequestParam(defaultValue = "DISTANCE") PathType type,
        @AuthenticationPrincipal LoginMember loginMember
    ) {
        return ResponseEntity.ok(pathService.findPath(source, target, type, loginMember));
    }
}
