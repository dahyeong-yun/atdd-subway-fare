package nextstep.subway.domain;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {
    private static final int BASE_FARE = 1250;
    private static final int BASE_DISTANCE = 10; // 10km
    private static final int MEDIUM_DISTANCE = 50; // 50km

    public int calculateFare(int totalDistance) {
        int fare = BASE_FARE;

        if (totalDistance <= BASE_DISTANCE) {
            return fare;
        }

        if (totalDistance <= MEDIUM_DISTANCE) {
            int extraDistance = totalDistance - BASE_DISTANCE;
            fare += (extraDistance / 5) * 100;
        } else {
            int mediumExtraDistance = MEDIUM_DISTANCE - BASE_DISTANCE;
            fare += (mediumExtraDistance / 5) * 100;

            int longExtraDistance = totalDistance - MEDIUM_DISTANCE;
            fare += (longExtraDistance / 8) * 100;
        }

        return fare;
    }
}
