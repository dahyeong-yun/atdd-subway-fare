package nextstep.subway.unit;


import nextstep.common.exception.SectionNotFoundException;
import nextstep.subway.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathTest {
    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;
    private Line 이호선;
    private Section section1;
    private Section section2;
    private List<Section> allSections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        삼성역 = new Station("삼성역");
        이호선 = new Line("2호선", "green");
        section1 = Section.createSection(이호선, 강남역, 역삼역, 2, 3);
        section2 = Section.createSection(이호선, 역삼역, 삼성역, 2, 3);
        allSections = Arrays.asList(section1, section2);
    }

    @Test
    @DisplayName("경로 생성 및 기본 속성 테스트")
    void createPath() {
        List<Station> stations = Arrays.asList(강남역, 역삼역, 삼성역);
        Path path = Path.createPath(stations, allSections, PathType.DISTANCE, 4);

        assertThat(path.getStations()).containsExactly(강남역, 역삼역, 삼성역);
        assertThat(path.getPathType()).isEqualTo(PathType.DISTANCE);
        assertThat(path.getTotalWeight()).isEqualTo(4);
        assertThat(path.getDistance()).isEqualTo(4);
        assertThat(path.getDuration()).isEqualTo(6);
    }

    @Test
    @DisplayName("요금 계산 테스트 - 기본 요금")
    void calculateFareBasic() {
        List<Station> stations = Arrays.asList(강남역, 역삼역);
        Path path = Path.createPath(stations, allSections, PathType.DISTANCE, 2);

        assertThat(path.getFare()).isEqualTo(1250);
    }

    @Test
    @DisplayName("요금 계산 테스트 - 추가 요금 (10km 초과)")
    void calculateFareExtra() {
        Section longSection = Section.createSection(이호선, 강남역, 삼성역, 12, 15);
        List<Section> sections = Arrays.asList(longSection);
        List<Station> stations = Arrays.asList(강남역, 삼성역);
        Path path = Path.createPath(stations, sections, PathType.DISTANCE, 12);

        assertThat(path.getFare()).isEqualTo(1350);  // 기본 요금 1250 + 추가 요금 100
    }

    @Test
    @DisplayName("요금 계산 테스트 - 추가 요금 (50km 초과)")
    void calculateFareLongDistance() {
        Section longSection = Section.createSection(이호선, 강남역, 삼성역, 55, 70);
        List<Section> sections = Arrays.asList(longSection);
        List<Station> stations = Arrays.asList(강남역, 삼성역);
        Path path = Path.createPath(stations, sections, PathType.DISTANCE, 55);

        assertThat(path.getFare()).isEqualTo(2150);  // 기본 요금 1250 + 추가 요금 300
    }

    @Test
    @DisplayName("유효한 경로 확인")
    void isValidPath() {
        List<Station> stations = Arrays.asList(강남역, 역삼역);
        Path path = Path.createPath(stations, allSections, PathType.DISTANCE, 2);

        assertThat(path.isValid()).isTrue();
    }

    @Test
    @DisplayName("유효하지 않은 경로 확인 - 빈 경로")
    void isInvalidPathEmpty() {
        List<Station> emptyStations = Arrays.asList();
        Path path = Path.createPath(emptyStations, allSections, PathType.DISTANCE, 0);

        assertThat(path.isValid()).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 경로 확인 - 가중치 0")
    void isInvalidPathZeroWeight() {
        List<Station> stations = Arrays.asList(강남역, 역삼역);
        Path path = Path.createPath(stations, allSections, PathType.DISTANCE, 0);

        assertThat(path.isValid()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 구간에 대한 예외 처리")
    void throwExceptionForNonExistentSection() {
        Station 잠실역 = new Station("잠실역");
        List<Station> invalidStations = Arrays.asList(강남역, 잠실역);

        assertThatThrownBy(() -> Path.createPath(invalidStations, allSections, PathType.DISTANCE, 10))
                .isInstanceOf(SectionNotFoundException.class);
    }
}
