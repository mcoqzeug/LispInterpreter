class Output {
    static String generateOutput(Node node) {
        if (node.left == null && node.right == null) {  // if node.left is null then node.right should also be null
            // an atom
            if (node.identifier == null) {
                // an integer
                return Integer.toString(node.integer);
            } else {
                // an identifier
                return node.identifier;
            }
        } else if (node.left == null || node.right == null) {
            return "ERROR: invalid node";
        }

        return "(" + generateOutput(node.left) + " . " + generateOutput(node.right) + ")";
    }
}
