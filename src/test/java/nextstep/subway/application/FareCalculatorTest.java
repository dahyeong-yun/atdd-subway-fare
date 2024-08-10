package nextstep.subway.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.domain.model.AgeGroup;
import nextstep.subway.domain.model.FareCalculator;
import nextstep.subway.domain.model.Line;

class FareCalculatorTest {
    private static final Line line1 = new Line(1L, "Line1", "Blue", 500);
    private static final Line line2 = new Line(2L, "Line2", "Green", 1000);
    private static final Line line3 = new Line(3L, "Line3", "Red", 700);

    private static final List<Line> allLines = List.of(line1, line2, line3);

    @Nested
    @DisplayName("최대 노선 추가 요금 계산 테스트")
    class CalculateMaxAdditionalFareTests {

        @Test
        @DisplayName("노선 하나가 있을 때 노선의 추가 요금 중 최대값을 리턴한다")
        void whenOneLine_thenReturnsMaxAdditionalFare() {
            // given
            List<Line> lines = List.of(line1);

            // when
            int maxAdditionalFare = FareCalculator.calculateMaxAdditionalFare(lines);

            // then
            assertThat(maxAdditionalFare).isEqualTo(500);
        }

        @Test
        @DisplayName("노선 두개가 있을 때 노선의 추가 요금 중 최대값을 리턴한다")
        void whenTwoLines_thenReturnsMaxAdditionalFare() {
            // given
            List<Line> lines = List.of(line1, line2);

            // when
            int maxAdditionalFare = FareCalculator.calculateMaxAdditionalFare(lines);

            // then
            assertThat(maxAdditionalFare).isEqualTo(1000);
        }

        @Test
        @DisplayName("노선 세개가 있을 때 노선의 추가 요금 중 최대값을 리턴한다")
        void whenThreeLines_thenReturnsMaxAdditionalFare() {
            // given
            List<Line> lines = List.of(line1, line2, line3);

            // when
            int maxAdditionalFare = FareCalculator.calculateMaxAdditionalFare(lines);

            // then
            assertThat(maxAdditionalFare).isEqualTo(1000);
        }
    }
    @ParameterizedTest
    @MethodSource("provideParametersForCalculateFareTest")
    @DisplayName("거리에 따라, 노선에 따라, 연령대에 따라 요금을 계산한다")
    void 요금_계산_테스트(int distance, AgeGroup ageGroup, int expectedBaseFare) {
        // when
        int fare = FareCalculator.calculateFare(distance, allLines, ageGroup);

        // then
        assertThat(fare).isEqualTo(expectedBaseFare);
    }

    static Stream<Object[]> provideParametersForCalculateFareTest() {
        return Stream.of(
            // BABY 그룹은 무조건 요금이 0원
            new Object[]{5, AgeGroup.BABY, 0},
            new Object[]{10, AgeGroup.BABY, 0},
            new Object[]{11, AgeGroup.BABY, 0},
            new Object[]{15, AgeGroup.BABY, 0},
            new Object[]{20, AgeGroup.BABY, 0},
            new Object[]{25, AgeGroup.BABY, 0},
            new Object[]{30, AgeGroup.BABY, 0},
            new Object[]{35, AgeGroup.BABY, 0},
            new Object[]{40, AgeGroup.BABY, 0},
            new Object[]{45, AgeGroup.BABY, 0},
            new Object[]{50, AgeGroup.BABY, 0},
            new Object[]{51, AgeGroup.BABY, 0},
            new Object[]{60, AgeGroup.BABY, 0},
            new Object[]{70, AgeGroup.BABY, 0},

            // CHILD 그룹 (최대 추가 요금 1000원 적용, 50% 할인 + 350원 공제 적용)
            new Object[]{5,  AgeGroup.CHILD, (int) ((1250 + 1000 - 350) * 0.5)},  // 기본 요금 + 최대 추가 요금
            new Object[]{10, AgeGroup.CHILD, (int) ((1250 + 1000 - 350) * 0.5)},  // 기본 요금 + 최대 추가 요금
            new Object[]{11, AgeGroup.CHILD, (int) ((1350 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 1회 + 최대 추가 요금
            new Object[]{15, AgeGroup.CHILD, (int) ((1350 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 1회 + 최대 추가 요금
            new Object[]{20, AgeGroup.CHILD, (int) ((1450 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 2회 + 최대 추가 요금
            new Object[]{25, AgeGroup.CHILD, (int) ((1550 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 3회 + 최대 추가 요금
            new Object[]{30, AgeGroup.CHILD, (int) ((1650 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 4회 + 최대 추가 요금
            new Object[]{35, AgeGroup.CHILD, (int) ((1750 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 5회 + 최대 추가 요금
            new Object[]{40, AgeGroup.CHILD, (int) ((1850 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 6회 + 최대 추가 요금
            new Object[]{45, AgeGroup.CHILD, (int) ((1950 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 7회 + 최대 추가 요금
            new Object[]{50, AgeGroup.CHILD, (int) ((2050 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 8회 + 최대 추가 요금
            new Object[]{51, AgeGroup.CHILD, (int) ((2150 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 9회 + 최대 추가 요금 + 50km 초과 요금 1회
            new Object[]{60, AgeGroup.CHILD, (int) ((2250 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 9회 + 최대 추가 요금 + 50km 초과 요금 2회
            new Object[]{70, AgeGroup.CHILD, (int) ((2350 + 1000 - 350) * 0.5)},  // 기본 요금 + 추가 요금 9회 + 최대 추가 요금 + 50km 초과 요금 3회

            // TEENAGER 그룹 (최대 추가 요금 1000원 적용, 20% 할인 + 350원 공제 적용)
            new Object[]{5,  AgeGroup.TEENAGER, (int) ((1250 + 1000 - 350) * 0.8)},  // 기본 요금 + 최대 추가 요금
            new Object[]{10, AgeGroup.TEENAGER, (int) ((1250 + 1000 - 350) * 0.8)},  // 기본 요금 + 최대 추가 요금
            new Object[]{11, AgeGroup.TEENAGER, (int) ((1350 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 1회 + 최대 추가 요금
            new Object[]{15, AgeGroup.TEENAGER, (int) ((1350 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 1회 + 최대 추가 요금
            new Object[]{20, AgeGroup.TEENAGER, (int) ((1450 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 2회 + 최대 추가 요금
            new Object[]{25, AgeGroup.TEENAGER, (int) ((1550 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 3회 + 최대 추가 요금
            new Object[]{30, AgeGroup.TEENAGER, (int) ((1650 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 4회 + 최대 추가 요금
            new Object[]{35, AgeGroup.TEENAGER, (int) ((1750 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 5회 + 최대 추가 요금
            new Object[]{40, AgeGroup.TEENAGER, (int) ((1850 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 6회 + 최대 추가 요금
            new Object[]{45, AgeGroup.TEENAGER, (int) ((1950 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 7회 + 최대 추가 요금
            new Object[]{50, AgeGroup.TEENAGER, (int) ((2050 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 8회 + 최대 추가 요금
            new Object[]{51, AgeGroup.TEENAGER, (int) ((2150 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 9회 + 최대 추가 요금 + 50km 초과 요금 1회
            new Object[]{60, AgeGroup.TEENAGER, (int) ((2250 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 9회 + 최대 추가 요금 + 50km 초과 요금 2회
            new Object[]{70, AgeGroup.TEENAGER, (int) ((2350 + 1000 - 350) * 0.8)},  // 기본 요금 + 추가 요금 9회 + 최대 추가 요금 + 50km 초과 요금 3회

            // ADULT 그룹 (최대 추가 요금 1000원 적용, 할인 없음)
            new Object[]{5,  AgeGroup.ADULT, 1250 + 1000},  // 기본 요금 + 최대 추가 요금
            new Object[]{10, AgeGroup.ADULT, 1250 + 1000},  // 기본 요금 + 최대 추가 요금
            new Object[]{11, AgeGroup.ADULT, 1350 + 1000},  // 기본 요금 + 추가 요금 1회 + 최대 추가 요금
            new Object[]{15, AgeGroup.ADULT, 1350 + 1000},  // 기본 요금 + 추가 요금 1회 + 최대 추가 요금
            new Object[]{20, AgeGroup.ADULT, 1450 + 1000},  // 기본 요금 + 추가 요금 2회 + 최대 추가 요금
            new Object[]{25, AgeGroup.ADULT, 1550 + 1000},  // 기본 요금 + 추가 요금 3회 + 최대 추가 요금
            new Object[]{30, AgeGroup.ADULT, 1650 + 1000},  // 기본 요금 + 추가 요금 4회 + 최대 추가 요금
            new Object[]{35, AgeGroup.ADULT, 1750 + 1000},  // 기본 요금 + 추가 요금 5회 + 최대 추가 요금
            new Object[]{40, AgeGroup.ADULT, 1850 + 1000},  // 기본 요금 + 추가 요금 6회 + 최대 추가 요금
            new Object[]{45, AgeGroup.ADULT, 1950 + 1000},  // 기본 요금 + 추가 요금 7회 + 최대 추가 요금
            new Object[]{50, AgeGroup.ADULT, 2050 + 1000},  // 기본 요금 + 추가 요금 8회 + 최대 추가 요금
            new Object[]{51, AgeGroup.ADULT, 2150 + 1000},  // 기본 요금 + 추가 요금 9회 + 최대 추가 요금 + 50km 초과 요금 1회
            new Object[]{60, AgeGroup.ADULT, 2250 + 1000},  // 기본 요금 + 추가 요금 9회 + 최대 추가 요금 + 50km 초과 요금 2회
            new Object[]{70, AgeGroup.ADULT, 2350 + 1000}   // 기본 요금 + 추가 요금 9회 + 최대 추가 요금 + 50km 초과 요금 3회
        );
    }
}
