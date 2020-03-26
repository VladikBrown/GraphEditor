package view;

import javafx.scene.control.Tab;

public class GraphTab extends Tab {
    private GraphView graphView;
    private EdgeList edgeList;

    public GraphTab(String title) {
        super(title);
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public EdgeList getEdgeList() {
        return edgeList;
    }
}
