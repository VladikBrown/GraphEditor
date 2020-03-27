package view;

import controller.EdgeController;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jdk.jfr.Experimental;
import model.EdgeModel;
import model.constants.VertexConst;

//TODO: Перенести реализацию в @EdgeController
//TODO: Upgrade
public class EdgeView extends Group {
    private GraphTab graphTab;
    //должно быть интовым!
    private Label weight;
    private Arrow view;
    private VertexView start;
    private VertexView finish;
    private EdgeModel edgeModel;
    public static EdgeController edgeController;



    //TODO: make private and add  getter/setter
    public EdgeView(GraphTab graphTab){
        this.edgeModel = new EdgeModel();
        initVertexControls();
        this.weight = new Label();
        this.view = new Arrow();
        this.graphTab = graphTab;
    }


    //TODO: довести до кондиций конструктор с параметрами
    public EdgeView(VertexView start, VertexView finish, GraphTab graphTab){
        initVertexControls();
        weight = new Label(" ");
        this.start = start;
        this.finish = finish;
        this.graphTab = graphTab;
        this.edgeModel = new EdgeModel();
        view = new Arrow(start, finish);
        this.getChildren().addAll(weight, view);
    }

    private void initVertexControls() {
        setOnMouseClicked(mouseEvent -> edgeController.setOnEdgeClicked(mouseEvent, graphTab, this));
    }

    public void setStart(VertexView start){
        this.start = start;
        this.edgeModel.setStart(start.getVertexModel());
        view.setStartPoint(start);
    }

    public void setFinish(VertexView finish) {
        this.finish = finish;
        view.setEndPoint(finish);
        this.edgeModel.setFinish(finish.getVertexModel());
        this.weight.translateXProperty().bind(((start.translateXProperty().add(finish.translateXProperty())).divide(2)));
        this.weight.translateYProperty().bind(((start.translateYProperty().add(finish.translateYProperty())).divide(2)));
        this.getChildren().addAll(weight, view);
    }

    public void setIdentifier(String id) {
        try {
            int value = Integer.parseInt(id);
            this.weight.setText(Integer.toString(value));
            this.edgeModel.setWeight(value);
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Invalid value, bro!");
            alert.setContentText("Weight of edge must be a number");
            alert.show();
        }
    }

    public EdgeModel getEdgeModel() {
        return edgeModel;
    }

    public void setEdgeModel(EdgeModel edgeModel) {
        this.edgeModel = edgeModel;
    }

    public String getIdentifier() {
        return weight.getText();
    }

    public VertexView getStart() {
        return start;
    }

    public Arrow getView() {
        return view;
    }

    public void setView(Arrow view) {
        this.view = view;
    }

    public VertexView getFinish() {
        return finish;
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
    private VertexView start, finish;
    private LineTo endPoint, tipLine1, tipLine2, tipLine3;
    private MoveTo startPoint;
    private DoubleProperty startXProperty, startYProperty, endXProperty, endYProperty;

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public Arrow(){
        super();
        strokeProperty().bind(fillProperty());
        setFill(Color.BLACK);
        this.setStrokeWidth(2);
    }

    public Arrow(VertexView start, VertexView finish, double arrowHeadSize){
        super();
        strokeProperty().bind(fillProperty());
        setFill(Color.BLACK);
        this.startX = start.getTranslateX() + VertexConst.VERTEX_SIZE_X/2.0;
        this.startY = start.getTranslateY() + VertexConst.VERTEX_SIZE_Y/2.0;
        this.endX = finish.getTranslateX() + VertexConst.VERTEX_SIZE_X/2.0;
        this.endY = finish.getTranslateY() + VertexConst.VERTEX_SIZE_Y/2.0;
        //Line
        this.startPoint = new MoveTo();
        this.endPoint = new LineTo();

        startPoint.xProperty().bind(start.translateXProperty().add(VertexConst.VERTEX_SIZE_X/2));
        startPoint.yProperty().bind(start.translateYProperty().add(VertexConst.VERTEX_SIZE_Y/2));

        endPoint.xProperty().bind(finish.translateXProperty().add(VertexConst.VERTEX_SIZE_X/2));
        endPoint.yProperty().bind(finish.translateYProperty().add(VertexConst.VERTEX_SIZE_Y/2));
        getElements().add(startPoint);
        getElements().add(endPoint);


        //Можно убрать параметр в конструткорах?? и вынести в отдельный метод

        // заменить всё следующее на buildEdge
        startXProperty = new SimpleDoubleProperty(startX);
        startXProperty.bind(start.translateXProperty().add(VertexConst.VERTEX_SIZE_X/2));
        startYProperty = new SimpleDoubleProperty(startY);
        startYProperty.bind(start.translateYProperty().add(VertexConst.VERTEX_SIZE_Y/2));
        endXProperty = new SimpleDoubleProperty(endX);
        endXProperty.bind(finish.translateXProperty().add(VertexConst.VERTEX_SIZE_X/2));
        endYProperty = new SimpleDoubleProperty(endY);
        endYProperty.bind(finish.translateYProperty().add(VertexConst.VERTEX_SIZE_Y/2));

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

    public void setStartPoint(VertexView start){
        this.start = start;
        this.startX = start.getTranslateX() + VertexConst.VERTEX_SIZE_X/2.0;
        this.startY = start.getTranslateY() + VertexConst.VERTEX_SIZE_X/2.0;
        this.startPoint = new MoveTo();
        startPoint.xProperty().bind(start.translateXProperty().add(VertexConst.VERTEX_SIZE_X/2));
        startPoint.yProperty().bind(start.translateYProperty().add(VertexConst.VERTEX_SIZE_Y/2));
        getElements().add(startPoint);
    }

    //можно добавить булевой параметр(да - выполнить метод билд, нет - не выполнять)
    public void setEndPoint(VertexView finish){
        this.finish = finish;
        this.endX = finish.getTranslateX() + VertexConst.VERTEX_SIZE_X/2.0;
        this.endY = finish.getTranslateY() + VertexConst.VERTEX_SIZE_X/2.0;
        this.endPoint = new LineTo();
        endPoint.xProperty().bind(finish.translateXProperty().add(VertexConst.VERTEX_SIZE_X/2));
        endPoint.yProperty().bind(finish.translateYProperty().add(VertexConst.VERTEX_SIZE_Y/2));
        getElements().add(endPoint);
        buildEdge();
    }

    // безполезное говно - переписать или убрать!

    private void updateTip(double x1, double y1, double x2, double y2, double endX, double endY){
        tipLine1.setX(x1);
        tipLine1.setY(y1);
        tipLine2.setX(x2);
        tipLine2.setY(y2);
        tipLine3.setX(endX);
        tipLine3.setY(endY);
    }

    public Arrow(VertexView start, VertexView finish){
        this(start, finish, defaultArrowHeadSize);
    }

    private void configureChangeListener(VertexView start, VertexView finish) {
        startX = start.getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        startY = start.getTranslateY() + VertexConst.VERTEX_SIZE_X / 2.0;
        endX = finish.getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        endY = finish.getTranslateY() + VertexConst.VERTEX_SIZE_X / 2.0;

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

    //обезопасить!
    private void buildEdge(){
        startXProperty = new SimpleDoubleProperty(startX);
        startXProperty.bind(start.translateXProperty().add(VertexConst.VERTEX_SIZE_X/2));
        startYProperty = new SimpleDoubleProperty(startY);
        startYProperty.bind(start.translateYProperty().add(VertexConst.VERTEX_SIZE_Y/2));
        endXProperty = new SimpleDoubleProperty(endX);
        endXProperty.bind(finish.translateXProperty().add(VertexConst.VERTEX_SIZE_X/2));
        endYProperty = new SimpleDoubleProperty(endY);
        endYProperty.bind(finish.translateYProperty().add(VertexConst.VERTEX_SIZE_Y/2));

        startXProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));
        startYProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));
        endXProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));
        endYProperty.addListener((observableValue, number, t1) -> configureChangeListener(start, finish));

        addTipLines();
    }
}

