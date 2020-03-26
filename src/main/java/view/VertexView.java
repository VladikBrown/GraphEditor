package view;

import Controller.VertexController;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import model.ModeState;
import model.VertexModel;
import model.constants.VertexConst;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class VertexView extends Label{
    private List<EdgeView> vertexEdges;
    Pane parentPane;
    ImageView icon;
    String identifier;
    ImageView greenVertexImageView;
    ImageView greyVertexImageView;
    ImageView orangeVertexImageView;
    private VertexModel vertexModel;
    //TODO make private
    public static VertexController vertexController;

    public VertexView(ImageView icon, String identifier, Pane pane){
        super(identifier, icon);
        this.vertexEdges = new ArrayList<EdgeView>();
        //vertexController = new VertexController();
        this.parentPane = pane;
        this.icon = icon;
        this.identifier = identifier;
        this.greenVertexImageView = new ImageView(VertexConst.GREEN_VERTEX_IMAGE);
        this.greyVertexImageView = new ImageView(VertexConst.GREY_VERTEX_IMAGE);
        this.orangeVertexImageView = new ImageView(VertexConst.ORANGE_VERTEX_IMAGE);
        initVertexControls();
    }

    private void initVertexControls(){
        this.setOnMouseEntered
                (mouseEvent -> this.setGraphic(greenVertexImageView));

        this.setOnMouseExited
                (mouseEvent -> this.setGraphic(greyVertexImageView));

        this.setOnMouseClicked
                (mouseEvent -> vertexController.setOnVertexClicked(mouseEvent, parentPane, this));

        this.setOnMousePressed
                (mouseEvent -> vertexController.setOnPressedVertex(mouseEvent, this));

        this.setOnMouseDragged
                (mouseEvent -> vertexController.setOnDragVertex(mouseEvent, this));
    }

    public void addEdge(EdgeView edge){
        vertexEdges.add(edge);
    }

    public  void removeEdge(EdgeView edge){
        vertexEdges.remove(edge);
    }

    public EdgeView getEdgesAtIndex(int x){
        if (x >= 0 && x < vertexEdges.size()){
            return vertexEdges.get(x);
        }
        else{
            return null;
        }
    }

    public VertexModel getVertexModel(){
        return this.vertexModel;
    }

    public int getNumberEdges(){
        return vertexEdges.size();
    }

    public void createVertex(Pane pane, MouseEvent mouseEvent){

    }

    public void setOrangeIcon(){
        this.setGraphic(orangeVertexImageView);
        this.setOnMouseExited
                (mouseEvent -> this.setGraphic(orangeVertexImageView));
    }

    public void setGreyIcon(){
        this.setGraphic(greyVertexImageView);
        this.setOnMouseExited
                (mouseEvent -> this.setGraphic(greyVertexImageView));
    }

    public void setIdentifier(String newID){
        this.setText(newID);
    }
}