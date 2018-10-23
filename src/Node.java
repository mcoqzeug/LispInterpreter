public class Node {
    public String identifier;
    public int integer;
    public Node left;
    public Node right;

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

    @Override
    public boolean equals(Object o) {
        // If the object is compared with itself then return true
        if (o == this)
            return true;

        /* Check if o is an instance of Node or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Node))
            return false;

        // typecast o to Node so that we can compare data members
        Node node = (Node) o;

        // Compare the data members and return accordingly
        if (identifier == null)
            return integer == node.integer;
        else
            return identifier.equals(node.identifier);
    }
}
