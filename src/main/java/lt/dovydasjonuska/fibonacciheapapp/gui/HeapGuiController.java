package lt.dovydasjonuska.fibonacciheapapp.gui;

import lt.dovydasjonuska.fibonacciheapapp.utils.FibonacciHeap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public class HeapGuiController {

    private FibonacciHeap<Integer> heap = new FibonacciHeap<>();

    @FXML
    private BorderPane root;

    @FXML
    private Canvas canvas;

    @FXML
    private TextArea outputLogs;

    @FXML
    private HBox topSection;

    @FXML
    private Button insertButton, extractMinButton;

    @FXML
    public void initialize() {
        log("Application started...");

        // Proportional element sizing
        canvas.widthProperty().bind(root.widthProperty().multiply(0.8));
        canvas.heightProperty().bind(root.heightProperty().multiply(0.7));
        outputLogs.prefWidthProperty().bind(root.widthProperty());
        outputLogs.prefHeightProperty().bind(root.heightProperty().multiply(0.3));
        topSection.prefWidthProperty().bind(root.widthProperty().multiply(0.2));
    }

    @FXML
    public void handleInsert() {
        log("Insert button clicked.");
        int random = (int) (Math.random() * 100);
        heap.insert(random);

        drawHeap();
    }

    @FXML
    public void handleExtractMin() {
        log("Extract Min button clicked.");

        heap.consolidate();
        drawHeap();
    }

    private void log(String message) {
        outputLogs.appendText(message + "\n"); // Append message to the log area
    }

    private void drawHeap() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Get drawing information from the heap
        FibonacciHeap<Integer>.DrawingInfo drawingInfo = heap.computeDrawingCoordinates();

        // Draw nodes
        for (FibonacciHeap.NodeCoordinates node : drawingInfo.nodes) {
            drawNode(gc, node.x, node.y, node.key.toString());
        }

        // Draw lines
        gc.setStroke(Color.GRAY);
        for (FibonacciHeap.LineCoordinates line : drawingInfo.lines) {
            gc.strokeLine(line.startX, line.startY, line.endX, line.endY);
        }
    }

    /**
     * Draws a single node on the canvas.
     * @param gc The GraphicsContext to draw on.
     * @param x The x-coordinate of the node.
     * @param y The y-coordinate of the node.
     * @param text The text to display inside the node.
     */
    private void drawNode(GraphicsContext gc, double x, double y, String text) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, 20, 20); // Draw node as a circle
        gc.setStroke(Color.BLACK);
        gc.strokeOval(x, y, 20, 20); // Draw the outline

        gc.setFill(Color.WHITE);
        gc.fillText(text, x + 5, y + 14); // Draw the text inside the node
    }

}
