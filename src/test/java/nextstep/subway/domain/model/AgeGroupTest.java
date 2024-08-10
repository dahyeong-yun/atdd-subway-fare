package nextstep.subway.domain.model;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AgeGroupTest {

    @DisplayName("0세에서 5세일 때 BABY AgeGroup이고 요금이 0원이 된다")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void testWhenAgeIsBetween0And5(int age) {
        // given
        int originalFare = 1000;

        // when
        AgeGroup ageGroup = AgeGroup.from(age);
        int discountedFare = ageGroup.calculateFare(originalFare);

        // then
        assertThat(ageGroup).isEqualTo(AgeGroup.BABY);
        assertThat(discountedFare).isEqualTo(0);
    }

    @DisplayName("6세에서 12세일 때 CHILD AgeGroup이고 요금이 할인되어 계산된다")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void testWhenAgeIsBetween6And12(int age) {
        // given
        int originalFare = 1000;

        // when
        AgeGroup ageGroup = AgeGroup.from(age);
        int discountedFare = ageGroup.calculateFare(originalFare);

        // then
        assertThat(ageGroup).isEqualTo(AgeGroup.CHILD);
        int expectedFare = (int) ((originalFare - 350) * 0.5);
        assertThat(discountedFare).isEqualTo(expectedFare);
    }

    @DisplayName("13세에서 18세일 때 TEENAGER AgeGroup이고 요금이 할인되어 계산된다")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void testWhenAgeIsBetween13And18(int age) {
        // given
        int originalFare = 1000;

        // when
        AgeGroup ageGroup = AgeGroup.from(age);
        int discountedFare = ageGroup.calculateFare(originalFare);

        // then
        assertThat(ageGroup).isEqualTo(AgeGroup.TEENAGER);
        int expectedFare = (int) ((originalFare - 350) * 0.8);
        assertThat(discountedFare).isEqualTo(expectedFare);
    }

    @DisplayName("19세 이상일 때 ADULT AgeGroup이고 요금이 할인 없이 계산된다")
    @ParameterizedTest
    @ValueSource(ints = {19, 20, 30, 40, 50, 60})
    void testWhenAgeIs19OrAbove(int age) {
        // given
        int originalFare = 1000;

        // when
        AgeGroup ageGroup = AgeGroup.from(age);
        int discountedFare = ageGroup.calculateFare(originalFare);

        // then
        assertThat(ageGroup).isEqualTo(AgeGroup.ADULT);
        assertThat(discountedFare).isEqualTo(originalFare);
    }
}
