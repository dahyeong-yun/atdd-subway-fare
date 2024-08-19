package nextstep.common.exception;

import nextstep.subway.domain.Station;
import org.springframework.http.HttpStatus;

public class SectionNotFoundException extends SubwayException {
    public SectionNotFoundException(Station upStation, Station downStation) {
        super(upStation.getName() + "과 " + downStation.getName() + "이 연결된 구간이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
