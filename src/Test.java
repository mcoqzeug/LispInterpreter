class Test {
    static void testOutput() {
        Node two = new Node(2);
        Node three = new Node(3);
        Node five = new Node(5);
        Node six = new Node(6);
        Node root = Eval.cons(Eval.cons(two, three), Eval.cons(five, six));
        System.out.println(Output.generateOutput(root));
    }
}
