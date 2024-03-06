package nextstep.subway.domain.path.fee;

public class Distance {

    private static final int MINIMUM_VALUE = 0;
    private final int value;

    public Distance(final int distance) {
        validationCheck(distance);

        this.value = distance;
    }

    public int value() {
        return value;
    }

    private void validationCheck(final int distance) {
        if (distance < MINIMUM_VALUE) {
            throw new IllegalArgumentException("요금 계산을 위한 거리가 음수 일 수 없습니다 : " + distance);
        }
    }

    public boolean isCurrentRange(final DistanceRange distanceRange) {
        return distanceRange.rangePredicate.test(this);
    }

    public boolean isLessThan(final int value) {
        return this.value < value;
    }

    public boolean isGreaterThan(final int value) {
        return this.value > value;
    }
}
