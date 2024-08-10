package nextstep.subway.domain.model;

import java.util.Arrays;

import nextstep.member.domain.Member;

public enum AgeGroup {
    BABY(0, 6, 1.0, 0),
    CHILD(6, 13, 0.5, 350),
    TEENAGER(13, 19, 0.2, 350),
    ADULT(19, Integer.MAX_VALUE, 0.0, 0),
    ;

    private final int minAge;
    private final int maxAge;
    private final double discountRate;
    private final int deduction;

    AgeGroup(int minAge, int maxAge, double discountRate, int deduction) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
        this.deduction = deduction;
    }

    public static AgeGroup from(int age) {
        return Arrays.stream(values())
            .filter(ageGroup -> age >= ageGroup.minAge && age < ageGroup.maxAge)
            .findFirst()
            .orElse(ADULT);
    }

    public static AgeGroup from(Member member) {
        if (member == null) {
            return ADULT;
        }

        return from(member.getAge());
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public int getDeduction() {
        return deduction;
    }

    public int calculateFare(int originalFare) {
        if (this == BABY) {
            return 0;
        }

        if (this == ADULT) {
            return originalFare;
        }

        int discountedFare = originalFare - this.deduction; // 고정 공제액 차감
        return (int) (discountedFare * (1 - discountRate));
    }
}
