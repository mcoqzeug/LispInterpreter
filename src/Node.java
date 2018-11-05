/**
 * This class represents an s-expression.
 *
 * @author  Juanxi Li
 * @version 1.0
 * @since   2018-10-24
 */

public class Node {
    String identifier;
    int integer;
    Node left;
    Node right;

    public Node(int value) {
        this.integer = value;
    }

    public Node(String value) {
        this.identifier = value;
    }

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
    }
}
