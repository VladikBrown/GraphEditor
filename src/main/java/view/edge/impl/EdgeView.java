package view.edge.impl;

import controller.EdgeController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import jdk.jfr.Experimental;
import model.constants.VertexConst;
import model.edge.EdgeModel;
import model.edge.IEdgeModel;
import view.edge.IEdgeView;
import view.graph.GraphTab;
import view.vertex.LabeledVertex;

//TODO: Перенести реализацию в @EdgeController
//TODO: Upgrade
public class EdgeView extends Group implements IEdgeView {
    private GraphTab<LabeledVertex, IEdgeView> graphTab;
    private Label weight;
    //private DrawableArc<VertexView> view;
    private Arrow view;
    private LabeledVertex start;
    private LabeledVertex finish;
    private IEdgeModel edgeModel;
    public static EdgeController edgeController;


    public EdgeView(GraphTab graphTab) {
        this.edgeModel = new EdgeModel();
        initVertexControls();
        this.weight = new Label();
        this.weight.setFont(new Font(20));
        this.view = new Arrow();
        //this.view = new DrawableArc<>();
        this.graphTab = graphTab;
        this.setCursor(Cursor.HAND);
    }

    private void initVertexControls() {
        setOnMouseClicked(mouseEvent -> edgeController.setOnEdgeClicked(graphTab, this));
    }

    @Override
    public int getIdentifier() {
        if (weight.getText().isEmpty()) {
            return 1;
        }
        return Integer.parseInt(weight.getText());
    }

    @Override
    public void setIdentifier(String id) {
        try {
            int value = Integer.parseInt(id);
            this.weight.setText(Integer.toString(value));
            this.edgeModel.setWeight(value);
        } catch (NumberFormatException e) {
            if (!"".equals(id)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Invalid value, bro!");
                alert.setContentText("Weight of edge must be a number");
                alert.show();
            }
        }
    }

    @Override
    public IEdgeModel getEdgeModel() {
        return edgeModel;
    }

    @Override
    public void setEdgeModel(IEdgeModel edgeModel) {
        this.edgeModel = edgeModel;
    }

    @Override
    public LabeledVertex getStart() {
        return start;
    }

    @Override
    public void setStart(LabeledVertex start) {
        this.start = start;
        this.edgeModel.setStart(start.getVertexModel());
        view.setStartPoint(start);
        //view.setStart(((VertexView) start));
    }

    @Override
    public LabeledVertex getFinish() {
        return finish;
    }

    @Override
    public void setFinish(LabeledVertex finish) {
        this.finish = finish;
        view.setEndPoint(finish);
        //view.setFinish(((VertexView) finish));
        this.edgeModel.setFinish(finish.getVertexModel());
        this.weight.translateXProperty().bind(((start.asLabel().translateXProperty().add(finish.asLabel().translateXProperty())).divide(2)));
        this.weight.translateYProperty().bind(((start.asLabel().translateYProperty().add(finish.asLabel().translateYProperty())).divide(2)));
        this.getChildren().addAll(weight, view);
    }

    public Arrow getView() {
        return view;
    }

    public void setView(Arrow view) {
        this.view = view;
    }

    @Override
    public void setEdgeColor(Color color) {
        this.view.setFill(color);
    }


    @Override
    public Node asNode() {
        return this;
    }

    @Override
    public void erase() {
        edgeController.deleteEdge(graphTab, this);
    }
}

//refactor!
@Experimental
class Arrow extends Path{
    {
        this.setOnMouseEntered(mouseEvent -> setFill(Color.GREEN));
        this.setOnMouseExited(mouseEvent -> setFill(Color.BLACK));
    }

    private static final double defaultArrowHeadSize = 8.0;
    private double startX, startY, endX, endY;
    private static final int LOOP_RADIUS = 50;
    private LineTo endPoint, tipLine1, tipLine2, tipLine3;
    private MoveTo startPoint;
    private DoubleProperty startXProperty, startYProperty, endXProperty, endYProperty;
    private static final int LINE_WIDTH = 3;
    private static final int ARROW_SIDE = 10;
    private static final int ARROW_SIDE_TO_HEIGHT_ANGLE = 20;
    private static final double SIN_Y = Math.sin(Math.toRadians(ARROW_SIDE_TO_HEIGHT_ANGLE));
    private static final double COS_Y = Math.cos(Math.toRadians(ARROW_SIDE_TO_HEIGHT_ANGLE));
    private LabeledVertex start, finish;
    private double headX;
    private double headY;

    private double headXMod;
    private double headYMod;
    private double leftXMod;
    private double leftYMod;
    private double rightXMod;
    private double rightYMod;

    private double cos;
    private double sin;

    public Arrow() {
        super();
        strokeProperty().bind(fillProperty());
        setFill(Color.BLACK);
        this.setStrokeWidth(2);
    }

    public Arrow(LabeledVertex start, LabeledVertex finish) {
        super();
        strokeProperty().bind(fillProperty());
        setFill(Color.BLACK);
        this.startX = start.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.startY = start.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_Y / 2.0;
        this.endX = finish.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.endY = finish.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_Y / 2.0;
        //Line
        this.startPoint = new MoveTo();
        this.endPoint = new LineTo();

        startPoint.xProperty().bind(start.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        startPoint.yProperty().bind(start.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));

        endPoint.xProperty().bind(finish.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        endPoint.yProperty().bind(finish.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));
        getElements().add(startPoint);
        getElements().add(endPoint);


        //Можно убрать параметр в конструткорах?? и вынести в отдельный метод
        // заменить всё следующее на buildEdge

        startXProperty = new SimpleDoubleProperty(startX);
        startXProperty.bind(start.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        startYProperty = new SimpleDoubleProperty(startY);
        startYProperty.bind(start.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));
        endXProperty = new SimpleDoubleProperty(endX);
        endXProperty.bind(finish.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        endYProperty = new SimpleDoubleProperty(endY);
        endYProperty.bind(finish.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));

        startXProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));
        startYProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));
        endXProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));
        endYProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));

        //ArrowHead
        addTipLines();
    }

    private double angle(double startX, double startY, double endX, double endY){
        return Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
    }

    public void setStartPoint(LabeledVertex start) {
        this.start = start;
        this.startX = start.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.startY = start.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.startPoint = new MoveTo();
        startPoint.xProperty().bind(start.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        startPoint.yProperty().bind(start.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));
        getElements().add(startPoint);
    }

    //можно добавить булевой параметр(да - выполнить метод билд, нет - не выполнять)
    public void setEndPoint(LabeledVertex finish) {
        this.finish = finish;
        this.endX = finish.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.endY = finish.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.endPoint = new LineTo();
        endPoint.xProperty().bind(finish.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        endPoint.yProperty().bind(finish.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));
        getElements().add(endPoint);
        buildEdge();
    }

    // безполезное говно - переписать или убрать!
    private void updateArrowTransform() {

        // cos = |endX - startX| / sqrt((endX - startX)^2 + (endY - startY)^2)
        cos = Math.abs(endX - startX)
                / Math.sqrt(Math.pow(endX - startX, 2)
                + Math.pow(endY - startY, 2));

        // sin = sqrt(1 - cos^2)
        sin = Math.sqrt(1 - Math.pow(cos, 2));


        headXMod = 10 * cos;
        headYMod = 10 * sin;

        headX = endX > startX ?
                endX - headXMod
                : endX + headXMod;

        headY = endY > startY ?
                endY - headYMod
                : endY + headYMod;

        // sin (90 - a - y) = cos a * COS_Y - sin a * SIN_Y &&& cos (90 - a - y) = sin a * COS_Y + cos a * SIN_Y =>>>
        rightXMod = ARROW_SIDE * (cos * COS_Y - sin * SIN_Y);
        rightYMod = ARROW_SIDE * (sin * COS_Y + cos * SIN_Y);

        //  cos (a - y) = cos a * COS_Y + sin a * SIN_Y &&& sin (a - y) = sin a * COS_Y - cos a * SIN_Y
        leftXMod = ARROW_SIDE * (cos * COS_Y + sin * SIN_Y);
        leftYMod = ARROW_SIDE * (sin * COS_Y - cos * SIN_Y);

        endX = headX;
        endY = headY;
    }

    private void updateTip(double x1, double y1, double x2, double y2, double endX, double endY) {
        tipLine1.setX(x1);
        tipLine1.setY(y1);
        tipLine2.setX(x2);
        tipLine2.setY(y2);
        tipLine3.setX(endX);
        tipLine3.setY(endY);
    }

    private void configureChangeListener(LabeledVertex start, LabeledVertex finish) {
        startX = start.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        startY = start.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_X / 2.0;
        endX = finish.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        endY = finish.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_X / 2.0;


        double newAngle = angle(startX, startY, endX, endY);

        double sin = Math.sin(newAngle);
        double cos = Math.cos(newAngle);

        //point1
        double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
        double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;

        updateTip(x1, y1, x2, y2, endX, endY);
    }

    private void addTipLines(){
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * defaultArrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * defaultArrowHeadSize + endY;

        this.tipLine1 = new LineTo(x1, y1);
        this.tipLine2 = new LineTo(x2, y2);
        this.tipLine3 = new LineTo(endX, endY);

        getElements().add(tipLine1);
        getElements().add(tipLine2);
        getElements().add(tipLine3);
    }

    private void buildEdge(){
        startXProperty = new SimpleDoubleProperty(startX);
        startXProperty.bind(start.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        startYProperty = new SimpleDoubleProperty(startY);
        startYProperty.bind(start.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));
        endXProperty = new SimpleDoubleProperty(endX);
        endXProperty.bind(finish.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        endYProperty = new SimpleDoubleProperty(endY);
        endYProperty.bind(finish.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));

        startXProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));
        startYProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));
        endXProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));
        endYProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));

        addTipLines();
    }
}

