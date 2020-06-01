package view.edge.impl;

import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import model.constants.VertexConst;
import view.vertex.LabeledVertex;

import java.util.Objects;
import java.util.Random;

@Deprecated
public class DrawableArc<DrawableNode extends LabeledVertex> {
    private static final Bloom BLOOM = new Bloom(0);

    private static final int LOOP_RADIUS = 50;
    private static final int LINE_WIDTH = 3;
    private static final int ARROW_SIDE = 10;
    private static final int ARROW_SIDE_TO_HEIGHT_ANGLE = 20;
    private static final double SIN_Y = Math.sin(Math.toRadians(ARROW_SIDE_TO_HEIGHT_ANGLE));
    private static final double COS_Y = Math.cos(Math.toRadians(ARROW_SIDE_TO_HEIGHT_ANGLE));


    private DrawableNode start;
    private DrawableNode finish;

    private boolean isFocused;

    private Polygon arrow;
    private Line line;
    private CubicCurve loop;
    private Color color;

    // Properties for arrow correct rotating and locating
    private double headX;
    private double headY;
    private double leftX;
    private double leftY;
    private double rightX;
    private double rightY;


    private double startX;
    private double startY;
    private double endX;
    private double endY;


    private double headXMod;
    private double headYMod;
    private double leftXMod;
    private double leftYMod;
    private double rightXMod;
    private double rightYMod;

    private double cos;
    private double sin;


    public DrawableArc() {

    }

    public DrawableArc(DrawableNode start, DrawableNode finish) {
        this.start = start;
        this.finish = finish;

        this.startX = start.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.startY = start.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_Y / 2.0;
        this.endX = finish.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.endY = finish.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_Y / 2.0;


        isFocused = false;

        Random randomColorComponent = new Random(System.currentTimeMillis());

        /*color = Color.color(
                randomColorComponent.nextDouble(),
                randomColorComponent.nextDouble(),
                randomColorComponent.nextDouble()
        );*/
        color = Color.BLACK;

        arrow = new Polygon();

        if (start.equals(finish)) {
            loop = new CubicCurve();
            configureLoop();

            line = new Line();
        } else {
            configureArrow();

            line = new Line(
                    startX, startY, endX, endY
            );
            configureLine();

            loop = new CubicCurve();
        }
    }

    public DrawableNode getStart() {
        return start;
    }

    public void setStart(DrawableNode start) {
        this.start = start;
        this.startX = start.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.startY = start.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_Y / 2.0;
    }

    public DrawableNode getFinish() {
        return finish;
    }

    public void setFinish(DrawableNode finish) {
        this.finish = finish;
        this.endX = finish.asLabel().getTranslateX() + VertexConst.VERTEX_SIZE_X / 2.0;
        this.endY = finish.asLabel().getTranslateY() + VertexConst.VERTEX_SIZE_Y / 2.0;


        isFocused = false;

        Random randomColorComponent = new Random(System.currentTimeMillis());

        /*color = Color.color(
                randomColorComponent.nextDouble(),
                randomColorComponent.nextDouble(),
                randomColorComponent.nextDouble()
        );*/
        color = Color.BLACK;

        arrow = new Polygon();

        if (start.equals(finish)) {
            loop = new CubicCurve();
            configureLoop();

            line = new Line();
        } else {
            configureArrow();

            line = new Line(
                    startX, startY, endX, endY
            );
            configureLine();

            loop = new CubicCurve();
        }
    }

    public Polygon getArrow() {
        return arrow;
    }

    public Line getLine() {
        return line;
    }

    public CubicCurve getLoop() {
        return loop;
    }


    public boolean isFocused() {
        return isFocused;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrawableArc that = (DrawableArc) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(finish, that.finish);
    }

    /*
        Configs
     */

    // Line configs: bindings, coloring
    private void configureLine() {
        line.setStrokeWidth(LINE_WIDTH);
        line.setStroke(color);


        line.startXProperty().bind(start.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        line.startYProperty().bind(start.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));
        line.endXProperty().bind(finish.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        line.endYProperty().bind(finish.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));
        arrow.translateXProperty().bind(start.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        arrow.translateYProperty().bind(start.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));

        // Line lightning when mouse entered
        line.setOnMouseEntered(e -> {
            line.setEffect(BLOOM);
            arrow.setEffect(BLOOM);
            isFocused = true;
        });

        // Remove lightning when mouse exited
        line.setOnMouseExited(e -> {
            line.setEffect(null);
            arrow.setEffect(null);
            isFocused = false;
        });
    }

    // Loop configs: binding, coloring
    private void configureLoop() {
        loop.setStrokeWidth(LINE_WIDTH);
        loop.setFill(Color.TRANSPARENT);
        loop.setStroke(color);

        loop.startXProperty().bind(start.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        loop.startYProperty().bind(start.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));
        loop.endXProperty().bind(finish.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        loop.endYProperty().bind(finish.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));
        loop.controlX1Property().bind(start.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2).add(-LOOP_RADIUS));
        loop.controlY1Property().bind(start.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2).add(-LOOP_RADIUS));
        loop.controlX2Property().bind(finish.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2));
        loop.controlY2Property().bind(finish.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2));

        // Line lightning when mouse entered
        loop.setOnMouseEntered(e -> {
            loop.setEffect(BLOOM);
            arrow.setEffect(BLOOM);
            isFocused = true;
        });

        // Remove lightning when mouse exited
        loop.setOnMouseExited(e -> {
            loop.setEffect(null);
            arrow.setEffect(null);
            isFocused = false;
        });
    }

    // Arrow configs: events handling, coloring
    private void configureArrow() {
        arrow.setStrokeWidth(LINE_WIDTH);
        arrow.setFill(color);
        arrow.setStroke(color);

        updateArrowShape();

        start.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2).addListener(change -> {
            updateArrowShape();
        });
        start.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2).addListener(change -> {
            updateArrowShape();
        });
        finish.asLabel().translateXProperty().add(VertexConst.VERTEX_SIZE_X / 2).addListener(change -> {
            updateArrowShape();
        });
        finish.asLabel().translateYProperty().add(VertexConst.VERTEX_SIZE_Y / 2).addListener(change -> {
            updateArrowShape();
        });

        // Arrow lightning when mouse entered
        arrow.setOnMouseEntered(e -> {
            arrow.setEffect(BLOOM);
            line.setEffect(BLOOM);
            loop.setEffect(BLOOM);
            isFocused = true;
        });

        // Remove lightning when mouse exited
        arrow.setOnMouseExited(e -> {
            arrow.setEffect(null);
            line.setEffect(null);
            loop.setEffect(null);
            isFocused = false;
        });
    }


    // Updates arrow coordinates if incident node was moved
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

        rightX = endX > startX ?
                headX - rightXMod
                : headX + rightXMod;
        rightY = endY > startY ?
                headY - rightYMod
                : headY + rightYMod;

        //  cos (a - y) = cos a * COS_Y + sin a * SIN_Y &&& sin (a - y) = sin a * COS_Y - cos a * SIN_Y
        leftXMod = ARROW_SIDE * (cos * COS_Y + sin * SIN_Y);
        leftYMod = ARROW_SIDE * (sin * COS_Y - cos * SIN_Y);

        leftX = endX > startX ?
                headX - leftXMod
                : headX + leftXMod;
        leftY = endY > startY ?
                headY - leftYMod
                : headY + leftYMod;
    }

    // Redraws arrow polygon
    private void updateArrowShape() {
        updateArrowTransform();

        arrow.getPoints().clear();
        arrow.getPoints().addAll(
                headX, headY,
                leftX, leftY,
                rightX, rightY,
                headX, headY
        );
    }
}