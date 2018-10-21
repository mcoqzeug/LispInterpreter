class Node {
    String identifier;
    int integer;
    Node left;
    Node right;

//    Node() { }

    Node(int value) {
        this.integer = value;
    }

    Node(String value) {
        this.identifier = value;
    }

    Node(Node left, Node right) {
        this.left = left;
        this.right = right;
    }
}
