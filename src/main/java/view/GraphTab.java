package view;

import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;

public class GraphTab extends Tab {
    private GraphView graphView;
    private EdgeList edgeList;
    private Pane currentPane;

    public GraphTab(String title) {
        super(title);
        edgeList = new EdgeList();
    }

    public void setPane(Pane pane){
        this.currentPane = pane;
        setContent(pane);
    }

    public Pane getPane(){
        return this.currentPane;
    }

    public void setGraphView(GraphView graphView) {
        this.graphView = graphView;
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public void addEdge(EdgeView edge){
        this.edgeList.addEdge(edge);
    }

    public EdgeList getEdgeList() {
        return edgeList;
    }
}
