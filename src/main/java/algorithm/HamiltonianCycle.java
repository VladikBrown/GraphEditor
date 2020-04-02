package algorithm;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import view.GraphTab;
import view.GraphView;
import view.VertexView;

import java.util.ArrayList;
import java.util.List;

public class HamiltonianCycle {
    GraphView graph;
    Pane graphView;
    VertexView currentVertex;
    List<Boolean> visitedVertex;
    public HamiltonianCycle(GraphTab graphTab){
        graph = graphTab.getGraphView();
        graphView = graphTab.getPane();
        visitedVertex = new ArrayList<>(graph.getVertexesGraph().size());
        
    }


}
