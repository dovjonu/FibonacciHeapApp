package lt.dovydasjonuska.fibonacciheapapp.gui;

import lt.dovydasjonuska.fibonacciheapapp.utils.FibonacciHeap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public class HeapGuiController {

    private FibonacciHeap<Integer> heap = new FibonacciHeap<>();

    @FXML
    private Canvas canvas; // Canvas for drawing

    @FXML
    private TextArea outputLogs; // Bottom text area for logs

    @FXML
    private Button insertButton, extractMinButton; // Example buttons

    @FXML
    public void initialize() {
        log("Application started...");

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
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas

        if (heap.min == null) {
            log("Heap is empty. Nothing to draw.");
            return;
        }

        // Draw all nodes in the root list
        drawSubtree(heap.min, 5, 5, gc);
    }

    /**
     * Recursively draws a subtree of the Fibonacci heap.
     * @param node The starting node of the subtree.
     * @param x The x-coordinate of the current node.
     * @param y The y-coordinate of the current node.
     * @param gc The GraphicsContext to draw on.
     */
    private int drawSubtree(FibonacciHeap<Integer>.Node node, int x, int y, GraphicsContext gc) {
        if (node == null) return 0;
        System.out.println("Drawing subtree for node: " + node.key);


        int offset = 0; // Track the horizontal offset for siblings
        FibonacciHeap<Integer>.Node current = node;
        boolean isFirstNode = true;

        do {
            System.out.println("Drawing node: " + current.key);
            int currentX = x + offset;

            // Draw the current node
            drawNode(gc, currentX, y, current.key.toString());

            // Draw line to the parent (centered beneath parent)
            if (current.parent != null) {
                gc.setStroke(Color.GRAY);
                gc.strokeLine(currentX + 10, y, x + 10, y - 50); // Centered connection
            }

            // Draw line to the left sibling if not the first node
            if (!isFirstNode) {
                gc.setStroke(Color.GRAY);
                gc.strokeLine(currentX - 50 + 10, y + 10, currentX + 10, y + 10); // Horizontal sibling connection
            }



            // Draw children recursively
            if (current.child != null) {
                if(!isFirstNode)
                    offset += drawSubtree(current.child, currentX, y + 50, gc); // Child centered directly beneath
                else
                    drawSubtree(current.child, currentX, y + 50, gc); // Child centered directly beneath
            }

            isFirstNode = false;

            offset += 50; // Add offset for siblings
            current = current.right;
        } while (current != node); // Loop through all siblings

        return offset;
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
