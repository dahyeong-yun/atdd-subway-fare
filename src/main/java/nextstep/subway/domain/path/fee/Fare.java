package nextstep.subway.domain.path.fee;

import java.util.Objects;

public class Fare {

    public static final int DEDUCTION_AMOUNT = 350;
    private final int value;

    public Fare(final int amount) {
        this.value = amount;
    }

    public static Fare deduction() {
        return new Fare(DEDUCTION_AMOUNT);
    }

    public int value() {
        return this.value;
    }

    public Fare add(final Fare fare) {
        return new Fare(this.value + fare.value);
    }

    public Fare minus(final Fare fare) {
        return new Fare(this.value - fare.value);
    }

    public Fare discount(double percent) {
        final Fare discountFare = new Fare((int) (this.value * (percent / 100)));
        return new Fare(this.value - discountFare.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}

