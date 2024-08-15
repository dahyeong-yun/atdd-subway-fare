package nextstep.subway.domain;

import lombok.RequiredArgsConstructor;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

@RequiredArgsConstructor
public class PathFinderFactory {
    public static PathFinder createPathFinder(List<Section> allSections, List<Station> allStations, PathType pathType) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        if(pathType == PathType.DISTANCE) {
            allStations.forEach(graph::addVertex);
            allSections.forEach(section -> graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getSectionDistance().getDistance()
            ));
        } else {
            allStations.forEach(graph::addVertex);
            allSections.forEach(section -> graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getSectionDuration()
            ));
        }
        return new PathFinder(graph);
    }
}
