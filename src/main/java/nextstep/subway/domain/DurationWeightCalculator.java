package nextstep.subway.domain;

public class DurationWeightCalculator implements SectionWeightCalculator {
    @Override
    public double calculateWeight(Section section) {
        return section.getSectionDuration();
    }
}
