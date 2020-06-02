package view.graph;

import controller.GraphController;
import model.GraphModel;
import view.edge.IEdgeView;
import view.vertex.IVertexView;

import java.util.ArrayList;
import java.util.List;

public class GraphView<V extends IVertexView, E extends IEdgeView> {
    private final GraphTab<V, E> graphTab;
    private GraphModel<V, E> graphRoot;
    private GraphController<V, E> graphController;
    private List<V> vertexesGraph;
    private V start;

    public GraphView(GraphTab<V, E> graphTab) {
        this.graphTab = graphTab;
        graphRoot = new GraphModel<>(this);
        vertexesGraph = new ArrayList<>();
        start = null;
    }

    public GraphTab<V, E> getGraphTab() {
        return graphTab;
    }

    public List<V> getVertexesGraph() {
        return vertexesGraph;
    }

    public void setVertexesGraph(List<V> vertexesGraph) {
        this.vertexesGraph = vertexesGraph;
    }

    public void addVertex(V newVertex) {
        vertexesGraph.add(newVertex);
        graphRoot.addVertex(newVertex);
        newVertex.setIdNum(vertexesGraph.size() - 1);
    }

    public void addEdge(V v1, V v2, E edgeView) {
        graphRoot.addEdge(v1, v2, edgeView);
    }

    public void removeVertex(V oldVertex) {
        vertexesGraph.remove(oldVertex);
        graphRoot.removeVertex(oldVertex);
    }

    public void removeEdge(E edge) {
        graphRoot.removeEdge(edge);
    }

    public GraphModel<V, E> getGraphRoot() {
        return graphRoot;
    }

    public void setGraphRoot(GraphModel<V, E> graphRoot) {
        this.graphRoot = graphRoot;
    }

    public V getStart() {
        return start;
    }

    public void setStart(V start) {
        this.start = start;
    }
}
