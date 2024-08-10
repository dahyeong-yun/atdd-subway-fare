package nextstep.subway.application.dto;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private Integer duration;
    private Integer additionalFare;


    public LineRequest() {
    }

    public LineRequest(
        String name,
        String color,
        Long upStationId,
        Long downStationId,
        Integer distance,
        Integer duration
    ) {
        this(name, color, upStationId, downStationId, distance, duration, 0);
    }

    public LineRequest(
        String name,
        String color,
        Long upStationId,
        Long downStationId,
        Integer distance,
        Integer duration,
        Integer additionalFare
    ) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
        this.additionalFare = additionalFare;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public void setUpStationId(Long upStationId) {
        this.upStationId = upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public void setDownStationId(Long downStationId) {
        this.downStationId = downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getAdditionalFare() {
        return additionalFare;
    }
}
