package model;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import model.edge.IEdgeModel;
import model.vertex.DefaultVertexModel;
import model.vertex.IVertexModel;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.cycle.HierholzerEulerianCycle;
import org.jgrapht.alg.planar.BoyerMyrvoldPlanarityInspector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import view.edge.IEdgeView;
import view.vertex.IVertexView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class GraphModel<V extends IVertexView, E extends IEdgeView> {
    private Graph<IVertexView, IEdgeView> graph;

    private List<IVertexModel> vertexesGraph;

    public GraphModel() {
        DefaultEdge defaultEdge = new DefaultEdge();

        graph = new DefaultDirectedGraph<>(IEdgeView.class);
        vertexesGraph = new ArrayList<>();
    }


    public void addVertex(V vertex) {
        graph.addVertex(vertex);
    }

    public void removeVertex(V vertex) {
        graph.removeVertex(vertex);
    }

    public void addEdge(V start, V finish, E edge) {
        graph.addEdge(start, finish, edge);
    }

    public void removeEdge(E edge) {
        graph.removeEdge(edge);
    }

    public void addVertex(DefaultVertexModel newVertex, int a) {
        vertexesGraph.add(newVertex);
    }

    public void removeVertex(IVertexModel oldVertex, int a) {
        vertexesGraph.remove(oldVertex);
    }

    public IVertexModel getVertexAtIndex(int x) {
        if (x >= 0 && x < vertexesGraph.size()) {
            return vertexesGraph.get(x);
        } else {
            return null;
        }
    }

    public int getVertexDegree(IVertexView vertexView) {
        return graph.degreeOf(vertexView);
    }

    public void getIncidenceMatrix() {
        ArrayList<TreeMap<String, String>> matrix = new ArrayList<>();
        List<IEdgeView> edgeList = new ArrayList<>(graph.edgeSet());
        List<IVertexView> vertexList = new ArrayList<>(graph.vertexSet());
        for (int i = 0; i < graph.edgeSet().size(); i++) {
            TreeMap<String, String> treeMap = new TreeMap<>();
            for (int j = 0; j < vertexList.size(); j++) {
                treeMap.put(vertexList.get(j).getIdentifier(), "0");
            }
            matrix.add(treeMap);
        }
        int counter = 0;
        for (var edge : edgeList) {
            TreeMap<String, String> currentMap = matrix.get(counter);
            String start = graph.getEdgeSource(edge).getIdentifier();
            String finish = graph.getEdgeTarget(edge).getIdentifier();
            currentMap.put(start, "1");
            currentMap.put(finish, "1");
            counter++;
        }
        ListView<String> listOfMatrix = new ListView<>();
        listOfMatrix.getItems().addAll(String.valueOf(matrix));

        StringBuilder fixedMatrix = new StringBuilder();
        for (var x : matrix) {
            fixedMatrix.append(x + "\n");
        }

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Incidence matrix");
        alert.setHeaderText("Incidence matrix: ");
        alert.setContentText(fixedMatrix.toString());
        alert.getButtonTypes().add(ButtonType.OK);
        alert.setWidth(500);
        alert.setResizable(true);
        alert.showAndWait();
    }

    public void findEulerianCycle() {
        HierholzerEulerianCycle<IVertexView, IEdgeView> eulerianCycle
                = new HierholzerEulerianCycle<>();
        if (eulerianCycle.isEulerian(graph)) {
            GraphPath<IVertexView, IEdgeView> path = eulerianCycle.getEulerianCycle(graph);
            highlightPath(path.getEdgeList());
            // highlightCycle(path.getVertexList());
        }
    }

    public int getDistance(IVertexView vertex1, IVertexView vertex2) {
        return findShortestPath(vertex1, vertex2).getEdgeList().stream().mapToInt(IEdgeView::getIdentifier).sum();
    }

    public GraphPath<IVertexView, IEdgeView> findShortestPath(IVertexView vertex1, IVertexView vertex2) {
        BFSShortestPath<IVertexView, IEdgeView> path = new BFSShortestPath<>(this.graph);
        var shortestPath = path.getPath(vertex1, vertex2);
        for (var x : graph.edgeSet()) {
            x.setEdgeColor(Color.BLACK);
        }
        for (var x : shortestPath.getEdgeList()) {
            x.setEdgeColor(Color.ORANGE);
        }
        return path.getPath(vertex1, vertex2);
    }

    public List<GraphPath<IVertexView, IEdgeView>> findAllPaths(IVertexView vertex1, IVertexView vertex2) {
        AllDirectedPaths<IVertexView, IEdgeView> paths = new AllDirectedPaths<>(this.graph);
        return paths.getAllPaths(vertex1, vertex2, true, null);
    }

    public List<Node> pathHighlightButtonFactory(List<GraphPath<IVertexView, IEdgeView>> paths) {
        List<Node> buttons = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            Button pathButton = new Button("Path " + (i + 1));
            int finalI = i;
            pathButton.setOnAction(actionEvent -> this.highlightPath(paths.get(finalI).getEdgeList()));
            buttons.add(pathButton);
        }
        return buttons;
    }

    public void isPlanar() {
        BoyerMyrvoldPlanarityInspector<IVertexView, IEdgeView> boyerMyrvoldPlanarityInspector
                = new BoyerMyrvoldPlanarityInspector<>(graph);
        while (!boyerMyrvoldPlanarityInspector.isPlanar()) {
            System.out.println("Not Planar!");
            this.graph.edgeSet().stream().findFirst().get().erase();
            boyerMyrvoldPlanarityInspector
                    = new BoyerMyrvoldPlanarityInspector<>(graph);
        }
        System.out.println("Planar!");
    }

    public int getVertexesGraphSize() {
        return vertexesGraph.size();
    }

    public boolean isEdgeRedudant(IEdgeView iEdgeView) {
        return graph.containsEdge(iEdgeView.getStart(), iEdgeView.getFinish());
    }

    private void printGraph() {
        int n = vertexesGraph.size();
        IVertexModel currentVertex;
        IEdgeModel currentEdge;

        for (int i = 0; i < n; i++) {
            currentVertex = vertexesGraph.get(i);
            System.out.println(currentVertex);

            int m = currentVertex.getEdgeSize();
            for (int j = 0; j < m; j++) {
                currentEdge = currentVertex.getEdgeAtIndex(j);
                System.out.println(currentEdge.getStart() + " " + currentEdge.getFinish() + " " + currentEdge.getWeight());
            }
        }
    }

    private void highlightPath(List<IEdgeView> path) {
        for (var edge : graph.edgeSet()) {
            edge.setEdgeColor(Color.BLACK);
        }
        for (var edge : path) {
            edge.setEdgeColor(Color.ORANGE);
        }
    }

    public void showInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Info about graph");
        alert.setContentText("Vertexes: " + graph.vertexSet().size() + "\nEdges: " + graph.edgeSet().size());
        alert.showAndWait();
    }
}