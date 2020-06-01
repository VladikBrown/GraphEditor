package algorithm;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import view.edge.IEdgeView;
import view.graph.GraphTab;
import view.graph.GraphView;
import view.vertex.IVertexView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Deprecated
public class HamiltonianCycle {
    private GraphView<IVertexView, IEdgeView> graph;
    private Pane graphView;
    private boolean[] visitedVertex;
    private List<LinkedList<IVertexView>> cycles;

    public HamiltonianCycle(GraphTab<IVertexView, IEdgeView> graphTab) {
        graph = graphTab.getGraphView();
        graphView = graphTab.getPane();
        visitedVertex = new boolean[graph.getVertexesGraph().size()];
        cycles = new LinkedList<>();

        for (var x : graph.getVertexesGraph()) {
            step(x, x, new LinkedList<>());
        }
        if (cycles.isEmpty()) {
            System.out.println("no ways");
        }
        for (var y : cycles) {
            System.out.println(y);
        }
    }

    public List<LinkedList<IVertexView>> getCycles() {
        return cycles;
    }

    public Button cycleButtonFactory(List<IVertexView> cycle, String buttonName) {
        Button cycleButton = new Button(buttonName);
        cycleButton.setOnAction(actionEvent -> this.highlightCycle(cycle));
        return cycleButton;
    }

    private void highlightCycle(List<IVertexView> cycle) {
        for (int i = 0; i < cycle.size() - 1; i++) {
            IVertexView fromVertex = cycle.get(i), toVertex = cycle.get(i + 1);
            fromVertex.getVertexEdges().stream().
                    filter((edgeView) -> edgeView.getFinish().equals(toVertex)).findFirst().get().setEdgeColor(Color.ORANGE);
        }
        cycle.get(cycle.size() - 1).getVertexEdges().stream().
                filter((edgeView) -> edgeView.getFinish().equals(cycle.get(0))).findFirst().get().setEdgeColor(Color.ORANGE);

    }

    private boolean step(IVertexView startVertex, IVertexView currentVertex, LinkedList<IVertexView> cycle) {
        if (isAllVertexesVisited() && currentVertex == startVertex) {
            cycles.add(cycle);
            Arrays.fill(visitedVertex, false);
            return true;
        }
        for (IEdgeView edge : currentVertex.getVertexEdges()) {
            if (edge.getStart().equals(currentVertex) && !isVertexVisited(edge.getFinish().getIdNum())) {
                cycle.add(edge.getFinish());
                visitedVertex[edge.getFinish().getIdNum()] = true;
                if (step(startVertex, edge.getFinish(), cycle)) {
                    return true;
                } else {
                    visitedVertex[edge.getFinish().getIdNum()] = false;
                    if (!cycle.isEmpty()) {
                        cycle.removeLast();
                    }
                    return false;
                }
            }
        }
        return false;
    }

    private boolean isVertexVisited(int vertexIdNum) {
        return visitedVertex[vertexIdNum];
    }

    private boolean isAllVertexesVisited() {
        for (boolean x : visitedVertex) {
            if (!x) {
                return false;
            }
        }
        return true;
    }
}

