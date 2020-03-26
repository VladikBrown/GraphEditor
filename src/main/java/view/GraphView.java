package view;

import model.GraphModel;

import java.util.ArrayList;
import java.util.List;

//start можно убрать
public class GraphView {
    private GraphModel graphRoot;
    public List<VertexView> getVertexesGraph() {
        return vertexesGraph;
    }

    public void setVertexesGraph(List<VertexView> vertexesGraph) {
        this.vertexesGraph = vertexesGraph;
    }

    private List<VertexView> vertexesGraph;
    private GraphTab graphTab;
    private VertexView start;

    public GraphView(GraphTab boxDrawing){
        this.graphTab = boxDrawing;
        graphRoot = new GraphModel();
        vertexesGraph = new ArrayList<VertexView>();
        start = null;
    }

    public void addVertex(VertexView newVertex){
        vertexesGraph.add(newVertex);
    }

    public void removeVertex(VertexView oldVertex){
        vertexesGraph.remove(oldVertex);
    }

    public GraphModel getGraphRoot() {
        return graphRoot;
    }

    public void setGraphRoot(GraphModel graphRoot) {
        this.graphRoot = graphRoot;
    }

    public VertexView getStart() {
        return start;
    }

    public void setStart(VertexView start) {
        this.start = start;
    }
}