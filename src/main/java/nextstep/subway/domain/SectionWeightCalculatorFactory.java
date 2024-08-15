package nextstep.subway.domain;

public class SectionWeightCalculatorFactory {
    public static SectionWeightCalculator getCalculator(PathType pathType) {
        if (pathType == PathType.DISTANCE) {
            return new DistanceWeightCalculator();
        }
        return new DurationWeightCalculator();
    }
}
