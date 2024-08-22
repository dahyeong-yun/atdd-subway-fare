package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    @Column(nullable = false)
    private SectionDistance sectionDistance;

    @Column
    private int sectionDuration;

    private Section(Line line, Station upStation, Station downStation, int distance, int duration) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.sectionDistance = new SectionDistance(distance);
        this.sectionDuration = duration;
    }

    public static Section createSection(Line line, Station upStation, Station downStation, int distance, int duration) {
        assert line != null;
        assert upStation != null;
        assert downStation != null;

        return new Section(line, upStation, downStation, distance, duration);
    }

    public boolean containsStations(Station upStation, Station downStation) {
        return (this.upStation.equals(upStation) && this.downStation.equals(downStation));
    }
}
