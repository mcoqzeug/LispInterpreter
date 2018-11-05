/**
 * This class converts a Node to a corresponding string.
 *
 * @author  Juanxi Li
 * @version 1.0
 * @since   2018-10-24
 */

class Output {
    static String generateOutput(Node node) {
        if (node.left == null && node.right == null) {  // if node.left is null then node.right should also be null
            if (node.identifier == null) return Integer.toString(node.integer);
            else return node.identifier;
        } else if (node.left == null || node.right == null)
            throw new IllegalArgumentException("ERROR: invalid node");
        return "(" + generateOutput(node.left) + " . " + generateOutput(node.right) + ")";
    }
}
