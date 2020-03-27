package fileManager;

import javafx.scene.image.ImageView;
import model.EdgeModel;
import model.VertexModel;
import model.constants.VertexConst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import view.EdgeList;
import view.EdgeView;
import view.GraphTab;
import view.VertexView;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;


public class GraphXML {
    private Element rootElement;
    private String nameFile;
    private GraphTab graphTab;
    private File newFile;

    public GraphXML(File file, view.GraphTab graphTab) {
        this.nameFile = nameFile;
        this.graphTab = graphTab;
        newFile = file;
    }

    public void writeFile() throws IOException, TransformerException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document document = builder.newDocument();

        rootElement = document.createElement(GraphTagXML.GRAPH);
        document.appendChild(rootElement);

        List<VertexView> vertexList = graphTab.getGraphView().getVertexesGraph();

        Element allvertexes = document.createElement(GraphTagXML.VERTEXS);
        rootElement.appendChild(allvertexes);

        Element allEdges = document.createElement(GraphTagXML.EDGES);
        rootElement.appendChild(allEdges);

        for (VertexView vertexView : vertexList) {
            Element currentVertex = document.createElement(GraphTagXML.VERTEX);
            currentVertex.setAttribute(GraphTagXML.NAMEVERTEX, vertexView.getIdentifier());

            String x = Double.toString(vertexView.getTranslateX());
            currentVertex.setAttribute(GraphTagXML.ALIGNMENTX, x);
            String y = Double.toString(vertexView.getTranslateY());
            currentVertex.setAttribute(GraphTagXML.ALIGNMENTY, y);
            allvertexes.appendChild(currentVertex);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Properties outFormat = new Properties();
            outFormat.setProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperties(outFormat);
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(newFile);
            transformer.transform(source, result);
        }

        EdgeList edges = graphTab.getEdgeList();
        int n = edges.getNumberAllEdges();
        for (int i = 0; i < n; i++) {
            EdgeView edge = edges.getEdgesAtIndex(i);
            Element currentEdge = document.createElement(GraphTagXML.EDGE);
            currentEdge.setAttribute(GraphTagXML.NAMEEDGE, edge.getIdentifier());

            String s = Double.toString(edge.getStart().getTranslateX());
            currentEdge.setAttribute(GraphTagXML.STARTALIGNMENTX, s);
            s = Double.toString(edge.getStart().getTranslateY());
            currentEdge.setAttribute(GraphTagXML.STARTALIGNMENTY, s);
            s = Double.toString(edge.getFinish().getTranslateX());
            currentEdge.setAttribute(GraphTagXML.FINISHALIGNMENTX, s);
            s = Double.toString(edge.getFinish().getTranslateY());
            currentEdge.setAttribute(GraphTagXML.FINISHALIGNMENTY, s);
            allEdges.appendChild(currentEdge);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Properties outFormat = new Properties();
            outFormat.setProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperties(outFormat);
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(newFile);
            transformer.transform(source, result);
        }
    }

    /*
    добавить функцию поиска вершины по координатам в GraphView
     */
    //TODO упростить создание VertexView через конструктор(без передачи ImageView)
    public void readFile(){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(newFile);
            document.getDocumentElement().normalize();

            NodeList vertexList = document.getElementsByTagName(GraphTagXML.VERTEX);
            for (int temp = 0; temp < vertexList.getLength(); temp++) {
                Node nNode = vertexList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String nameVertex = eElement.getAttribute(GraphTagXML.NAMEVERTEX);
                    VertexView vertex = new VertexView(new ImageView(VertexConst.GREY_VERTEX_IMAGE), nameVertex, graphTab);

                    VertexModel newVertexModel = new VertexModel();
                    vertex.setVertexModel(newVertexModel);
                    graphTab.getGraphView().getGraphRoot().addVertex(newVertexModel);
                    graphTab.getGraphView().addVertex(vertex);
                    if (graphTab.getGraphView().getStart() == null){
                        graphTab.getGraphView().setStart(vertex);
                    }

                    double x = Double.parseDouble(eElement.getAttribute(GraphTagXML.ALIGNMENTX));
                    double y = Double.parseDouble(eElement.getAttribute(GraphTagXML.ALIGNMENTY));

                    vertex.setTranslateX(x);
                    vertex.setTranslateY(y);
                    graphTab.getPane().getChildren().add(vertex);
                }
            }

            NodeList edgeList = document.getElementsByTagName(GraphTagXML.EDGE);
            for (int temp = 0; temp < edgeList.getLength(); temp++) {
                Node nNode = edgeList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    EdgeView newEdge = new EdgeView(graphTab);
                    newEdge.setIdentifier(eElement.getAttribute(GraphTagXML.NAMEEDGE));
                    double x1 = Double.parseDouble(eElement.getAttribute(GraphTagXML.STARTALIGNMENTX));
                    double y1 = Double.parseDouble(eElement.getAttribute(GraphTagXML.STARTALIGNMENTY));
                    double x2 = Double.parseDouble(eElement.getAttribute(GraphTagXML.FINISHALIGNMENTX));
                    double y2 = Double.parseDouble(eElement.getAttribute(GraphTagXML.FINISHALIGNMENTY));


                    //TODO вынести в отдельный метод
                    Optional <VertexView> startOptional = findVertexWithAlignment(x1, y1);

                    startOptional.ifPresent(vertexView -> {
                        newEdge.setStart(((startOptional.get())));
                        startOptional.get().addEdge(newEdge);
                    });

                    Optional<VertexView> finishOptional = findVertexWithAlignment(x2, y2);
                    finishOptional.ifPresent(vertexView -> {
                        newEdge.setFinish(((finishOptional.get())));
                        finishOptional.get().addEdge(newEdge);
                    });

                    graphTab.addEdge(newEdge);

                    EdgeModel newEdgeModel = new EdgeModel(newEdge.getStart().getVertexModel(),newEdge.getFinish().getVertexModel());
                    newEdge.setEdgeModel(newEdgeModel);
                    newEdge.getStart().getVertexModel().addEdge(newEdgeModel);
                    newEdge.getFinish().getVertexModel().addEdge(newEdgeModel);
                    graphTab.getPane().getChildren().add(newEdge);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<VertexView> findVertexWithAlignment(double x1, double y1){
        return graphTab.getGraphView().getVertexesGraph().stream().
                filter(v1 -> v1.getTranslateX() == x1 && v1.getTranslateY() == y1).findFirst();
    }


}

