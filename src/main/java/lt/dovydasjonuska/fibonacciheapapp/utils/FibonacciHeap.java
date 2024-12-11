package lt.dovydasjonuska.fibonacciheapapp.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FibonacciHeap<T extends Comparable<T>> {
    private Node min;
    private int size;

    private class Node {
        public T key;
        public int degree;
        public boolean childCut;
        public Node parent;
        public Node child;
        public Node left;
        public Node right;

        public Node(T key) {
            this.key = key;
            this.degree = 0;
            this.childCut = false;
            this.parent = null;
            this.child = null;
            this.left = this;
            this.right = this;
        }
    }

    public FibonacciHeap() {
        this.min = null;
        this.size = 0;
    }

    public void insert(T key) {
        Node node = new Node(key);
        if (min == null) {
            min = node;
        } else {
            // Add node to the root list
            node.right = min;
            node.left = min.left;
            min.left.right = node;
            min.left = node;
            if (key.compareTo(min.key) < 0) {
                min = node;
            }
        }
        size++;
    }

    public T findMin() {
        return min != null ? min.key : null;
    }

    public T extractMin() {
        if (min == null) return null;
        Node z = min;
        if (z.child != null) {
            // Add children of z to the root list
            Node child = z.child;
            do {
                child.parent = null;
                child = child.right;
            } while (child != z.child);
            Node zLeft = z.left;
            Node childLeft = z.child.left;
            z.left.right = z.child;
            z.child.left = zLeft;
            childLeft.right = z;
            z.left = childLeft;
        }

        // Remove z from the root list
        z.left.right = z.right;
        z.right.left = z.left;

        if (z == z.right) {
            min = null;
        } else {
            min = z.right;
            consolidate();
        }

        size--;
        return z.key;
    }

    public void consolidate() {
        int maxDegree = (int) Math.floor(Math.log(size) / Math.log(2)) + 1;
        ArrayList<Node> degrees = new ArrayList<>(Collections.nCopies(maxDegree, null));

        ArrayList<Node> rootNodes = getRootNodes();
        for (Node w : rootNodes) {
            Node x = w;
            int d = x.degree;
            while (degrees.get(d) != null) {
                Node y = degrees.get(d);
                if (x.key.compareTo(y.key) > 0) {
                    Node temp = x;
                    x = y;
                    y = temp;
                }
                link(y, x);
                degrees.set(d, null);
                d++;
            }
            degrees.set(d, x);
        }

        min = null;
        for (Node node : degrees) {
            if (node != null) {
                if (min == null || node.key.compareTo(min.key) < 0) {
                    min = node;
                }
            }
        }
    }

    private void link(Node y, Node x) {
        // Remove y from the root list
        y.left.right = y.right;
        y.right.left = y.left;

        // Make y a child of x
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.left = y;
            y.right = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }
        x.degree++;
        y.childCut = false;
    }

    public void decreaseKey(T key, T newKey) {
        Node node = findNode(min, key);
        if (node == null) {
            throw new IllegalArgumentException("Node with key " + key + " not found");
        }
        decreaseKey(node, newKey);
    }

    private void decreaseKey(Node x, T newKey) {
        if (newKey.compareTo(x.key) > 0) {
            throw new IllegalArgumentException("New key is greater than current key");
        }
        x.key = newKey;
        Node y = x.parent;
        if (y != null && x.key.compareTo(y.key) < 0) {
            cut(x, y);
            cascadingCut(y);
        }
        if (x.key.compareTo(min.key) < 0) {
            min = x;
        }
    }

    public void remove(T key) {
        Node node = findNode(min, key);
        if (node == null) {
            throw new IllegalArgumentException("Node with key " + key + " not found");
        }
        remove(node, node.parent);
    }

    private void remove(Node x, Node y) {
        // Remove x from y's child list
        if (x.right == x) {
            y.child = null;
        } else {
            x.right.left = x.left;
            x.left.right = x.right;
            if (y.child == x) {
                y.child = x.right;
            }
        }
        x.right = x;
        x.left = x;
        x.parent = null;
        if(x.child != null)
            link(x.child, y);
        y.degree--;
    }

    private void cut(Node x, Node y) {
        // Remove x from y's child list
        if (x.right == x) {
            y.child = null;
        } else {
            x.right.left = x.left;
            x.left.right = x.right;
            if (y.child == x) {
                y.child = x.right;
            }
        }
        y.degree--;

        // Add x to the root list
        x.left = min.left;
        x.right = min;
        min.left.right = x;
        min.left = x;

        x.parent = null;
        x.childCut = false;
    }

    private void cascadingCut(Node y) {
        Node z = y.parent;
        if (z != null) {
            if (!y.childCut) {
                y.childCut = true;
            } else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }

    public void union(FibonacciHeap<T> other) {
        if (other == null || other.min == null) return;
        if (min == null) {
            min = other.min;
            size = other.size;
        } else {
            // Merge root lists
            Node thisLeft = min.left;
            Node otherLeft = other.min.left;
            min.left = otherLeft;
            otherLeft.right = min;
            other.min.left = thisLeft;
            thisLeft.right = other.min;

            // Update min
            if (other.min.key.compareTo(min.key) < 0) {
                min = other.min;
            }

            size += other.size;
        }
    }

    private ArrayList<Node> getRootNodes() {
        ArrayList<Node> rootNodes = new ArrayList<>();
        if (min == null) return rootNodes;

        Node current = min;
        do {
            rootNodes.add(current);
            current = current.right;
        } while (current != min);

        return rootNodes;
    }

    public int size() {
        return size;
    }

    private Node findNode(Node node, T key) {
        if (node == null) return null;
        Node current = node;
        do {
            if (current.key.equals(key)) {
                return current;
            }
            if(current.child != null){
                Node found = findNode(current.child, key);
                if(found != null){
                    return found;
                }
            }
            current = current.right;
        } while (current != node);

        return null;
    }

    /// Drawing methods and classes:

    // Inner class to store drawing information
    public class DrawingInfo {
        public final List<NodeCoordinates> nodes = new ArrayList<>();
        public final List<LineCoordinates> lines = new ArrayList<>();
    }

    public class NodeCoordinates {
        public final T key;
        public final int x;
        public final int y;

        public NodeCoordinates(T key, int x, int y) {
            this.key = key;
            this.x = x;
            this.y = y;
        }
    }

    public class LineCoordinates {
        public final int startX;
        public final int startY;
        public final int endX;
        public final int endY;

        public LineCoordinates(int startX, int startY, int endX, int endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }
    }

    public DrawingInfo computeDrawingCoordinates() {
        DrawingInfo drawingInfo = new DrawingInfo();
        if (min == null) return drawingInfo;

        computeSubtreeCoordinates(min, 5, 5, drawingInfo, null);
        return drawingInfo;
    }

    private int computeSubtreeCoordinates(Node node, int x, int y, DrawingInfo drawingInfo, Node parent) {
        if (node == null) return 0;

        int totalWidth = 0;
        int currentX = x;
        int maxHeight = 50; // Spacing between parent and children

        Node current = node;
        do {
            // Compute child subtree width
            int childWidth = 0;
            if (current.child != null) {
                childWidth = computeSubtreeCoordinates(current.child, currentX, y + maxHeight, drawingInfo, current);
            }

            // Use max(childWidth, 50) for node spacing
            int subtreeWidth = Math.max(childWidth, 50);
            totalWidth += subtreeWidth;

            // Store current node position
            drawingInfo.nodes.add(new NodeCoordinates(current.key, currentX, y));

            // Store line to parent
            if (parent != null) {
                drawingInfo.lines.add(new LineCoordinates(currentX + 10, y, x + 10, y - maxHeight));
            }

            // Update current X for the next sibling
            currentX += subtreeWidth;
            current = current.right;

        } while (current != node);

        return totalWidth;
    }
}