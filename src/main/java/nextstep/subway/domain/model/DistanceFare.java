package nextstep.subway.domain.model;

import static nextstep.subway.domain.model.FareCalculator.*;

public enum DistanceFare {
    BASE {
        @Override
        public int calculateFare(int distance) {
            return BASE_FARE;
        }
    },
    ELEVEN_TO_FIFTY {
        @Override
        public int calculateFare(int distance) {
            int extraFare = (int) Math.ceil((distance - DISTANCE_THRESHOLD_10KM) / (double) UNIT_DISTANCE_5KM) * EXTRA_FARE_PER_5KM;
            return BASE_FARE + extraFare;
        }
    },
    OVER_FIFTY {
        @Override
        public int calculateFare(int distance) {
            int extraFare8km = (int) Math.ceil((distance - DISTANCE_THRESHOLD_50KM) / (double) UNIT_DISTANCE_8KM) * EXTRA_FARE_PER_8KM;
            return BASE_FARE + MAX_EXTRA_FARE_11KM_TO_50KM + extraFare8km;
        }
    };

    public abstract int calculateFare(int distance);

    public static DistanceFare from(int distance) {
        if (distance <= DISTANCE_THRESHOLD_10KM) {
            return BASE;
        }

        if (distance <= DISTANCE_THRESHOLD_50KM) {
            return ELEVEN_TO_FIFTY;
        }

        return OVER_FIFTY;
    }
}
