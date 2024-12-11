package lt.dovydasjonuska.fibonacciheapapp.gui;

import javafx.scene.input.MouseEvent;
import lt.dovydasjonuska.fibonacciheapapp.utils.FibonacciHeap;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public class HeapGuiController {

    private FibonacciHeap<Integer> heap = new FibonacciHeap<>();

    private int seed = 123456;

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
    private TextField generateSizeField, key1, key2, deleteKeyField;

    @FXML
    public void initialize() {
        log("Application started...");

        generateSizeField.setText("15");

        // Proportional element sizing
        canvas.widthProperty().bind(root.widthProperty().multiply(0.8));
        canvas.heightProperty().bind(root.heightProperty().multiply(0.7));
        outputLogs.prefWidthProperty().bind(root.widthProperty());
        outputLogs.prefHeightProperty().bind(root.heightProperty().multiply(0.3));
        topSection.prefWidthProperty().bind(root.widthProperty().multiply(0.2));
    }

    @FXML
    public void handleCanvasClicked() {
        log("Canvas clicked.");
    }

    @FXML
    public void handleGenerateRandomHeap() {
        log("Generated random heap with size: " + generateSizeField.getText());
        Random generator = new Random(seed);
        heap = new FibonacciHeap<>();
        int size = Integer.parseInt(generateSizeField.getText());
        for (int i = 0; i < size; i++) {
            heap.insert(generator.nextInt(100));
        }
        drawHeap();
    }

    @FXML
    public void handleInsert() {
        int random = (int) (Math.random() * 100);
        log("Inserted: " + random);
        heap.insert(random);

        drawHeap();
    }

    @FXML
    public void handleGetMin() {
        log("Min: " + heap.findMin().toString());

        drawHeap();
    }

    @FXML
    public void handleExtractMin() {
        log("Min Extracted: " + heap.extractMin().toString());

        drawHeap();
    }

    @FXML
    public void handleConsolidate() {
        log("Consolidated.");

        heap.consolidate();
        drawHeap();
    }

    @FXML
    public void handleUnion() {
        int size = Integer.parseInt(generateSizeField.getText());
        FibonacciHeap<Integer> heap2 = new FibonacciHeap();
        for (int i = 0; i < size; i++) {
            heap2.insert((int) (Math.random() * 100));
        }
        heap2.consolidate();

        heap.union(heap2);
        log("Unionised with a new random consolidated heap of size: " + size);
        drawHeap();
    }

    @FXML
    public void handleDecreaseKey() {
        int key1int = Integer.parseInt(this.key1.getText());
        int key2int = Integer.parseInt(this.key2.getText());

        heap.decreaseKey(key1int, key2int);

        log("Node with key: " + key1int + " decreased to: " + key2int);
        drawHeap();
    }

    @FXML
    public void handleDelete() {
        int key = Integer.parseInt(this.deleteKeyField.getText());
        heap.remove(key);

        log("Node with key: " + key + " deleted.");
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

        gc.getCanvas().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            log("Node clicked: " + text);
        });
    }

}
