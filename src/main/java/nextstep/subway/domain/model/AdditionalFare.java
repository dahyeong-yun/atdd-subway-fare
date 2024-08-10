package nextstep.subway.domain.model;

import java.util.Objects;

import javax.persistence.Column;

public class AdditionalFare {
    public static final String INVALID_ADDITIONAL_FARE_MESSAGE = "추가 요금은 0이상이어야 합니다.";

    @Column(name = "additionalFare", nullable = false)
    private Integer value;

    protected AdditionalFare() {
    }

    public AdditionalFare(Integer value) {
        if (value < 0) {
            throw new IllegalArgumentException(INVALID_ADDITIONAL_FARE_MESSAGE);
        }

        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        AdditionalFare that = (AdditionalFare)object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
