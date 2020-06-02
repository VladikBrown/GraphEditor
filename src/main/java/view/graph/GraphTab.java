package view.graph;

import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import view.edge.EdgeList;
import view.edge.IEdgeView;
import view.vertex.IVertexView;

public class GraphTab<V extends IVertexView, E extends IEdgeView> extends Tab {
    private GraphView<V, E> graphView;
    private EdgeList edgeList;
    private Pane currentPane;

    public GraphTab(String title) {
        super(title);
        edgeList = new EdgeList();
    }

    public void setPane(Pane pane) {
        this.currentPane = pane;
    }

    public Pane getPane() {
        return this.currentPane;
    }

    public GraphView<V, E> getGraphView() {
        return graphView;
    }

    public void setGraphView(GraphView<V, E> graphView) {
        this.graphView = graphView;
    }

    public void addEdge(V vertex1, V vertex2, E edge) {
        this.edgeList.addEdge(edge);
        graphView.addEdge(vertex1, vertex2, edge);
        currentPane.getChildren().add(edge.asNode());
    }

    public EdgeList getEdgeList() {
        return edgeList;
    }
}
