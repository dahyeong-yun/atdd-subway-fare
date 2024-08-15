package nextstep.subway.domain;

public class DistanceWeightCalculator implements SectionWeightCalculator {
    @Override
    public double calculateWeight(Section section) {
        return section.getSectionDistance().getDistance();
    }
}
