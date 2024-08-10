package nextstep.subway.domain.model;

import java.util.List;

public class FareCalculator {
    public static final int BASE_FARE = 1250;
    public static final int EXTRA_FARE_PER_5KM = 100;
    public static final int EXTRA_FARE_PER_8KM = 100;
    public static final int DISTANCE_THRESHOLD_10KM = 10;
    public static final int DISTANCE_THRESHOLD_50KM = 50;
    public static final int UNIT_DISTANCE_5KM = 5;
    public static final int UNIT_DISTANCE_8KM = 8;
    public static final int MAX_EXTRA_FARE_11KM_TO_50KM =
        (int) Math.ceil((DISTANCE_THRESHOLD_50KM - DISTANCE_THRESHOLD_10KM) / (double) UNIT_DISTANCE_5KM) * EXTRA_FARE_PER_5KM;

    public static int calculateFare(int distance, List<Line> lines, AgeGroup ageGroup) {
        int fare = DistanceFare.from(distance).calculateFare(distance);
        fare += calculateMaxAdditionalFare(lines);
        return ageGroup.calculateFare(fare);
    }

    public static int calculateMaxAdditionalFare(List<Line> lines) {
        return lines.stream()
            .mapToInt(Line::getAdditionalFare)
            .max()
            .orElse(0);
    }
}