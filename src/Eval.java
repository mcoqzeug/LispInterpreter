import java.util.HashMap;
import java.util.Map;

class Eval {
    static final Node NIL = new Node("NIL");
    private static final Node T = new Node("T");

    private static final Node PLUS = new Node("PLUS");
    private static final Node MINUS = new Node("MINUS");
    private static final Node LESS = new Node("LESS");
    private static final Node TIMES = new Node("TIMES");
    private static final Node GREATER = new Node("GREATER");
    private static final Node EQ = new Node("EQ");
    private static final Node CONS = new Node("CONS");
    private static final Node CAR = new Node("CAR");
    private static final Node CDR = new Node("CDR");
    private static final Node ATOM = new Node("ATOM");
    private static final Node QUOTE = new Node("QUOTE");
    private static final Node COND = new Node("COND");
    private static final Node DEFUN = new Node("DEFUN");

    static final Map<String, Node> primitives;
    static {
        primitives = new HashMap<>();
        primitives.put("NIL", NIL);
        primitives.put("T", T);
        primitives.put("PLUS", PLUS);
        primitives.put("MINUS", MINUS);
        primitives.put("LESS", LESS);
        primitives.put("TIMES", TIMES);
        primitives.put("GREATER", GREATER);
        primitives.put("EQ", EQ);
        primitives.put("CONS", CONS);
        primitives.put("CAR", CAR);
        primitives.put("CDR", CDR);
        primitives.put("ATOM", ATOM);
        primitives.put("QUOTE", QUOTE);
        primitives.put("COND", COND);
        primitives.put("DEFUN", DEFUN);
    }

    static Map<String, Node> ids = new HashMap<>(primitives);


    static Node cons(Node left, Node right) {
        return new Node(left, right);
    }

    static Node addID(String id) {
        Node newNode = new Node(id);
        ids.put(id, newNode);
        return newNode;
    }
}
