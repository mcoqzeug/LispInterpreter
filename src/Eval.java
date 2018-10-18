import java.util.HashMap;
import java.util.Map;

public class Eval {
    // atoms
    public static final Node NIL = new Node("NIL");
    public static final Node T = new Node("T");

    // other identifiers
    public static final Node PLUS = new Node("PLUS");
    public static final Node MINUS = new Node("MINUS");
    public static final Node LESS = new Node("LESS");
    public static final Node TIMES = new Node("TIMES");
    public static final Node GREATER = new Node("GREATER");
    public static final Node EQ = new Node("EQ");
    public static final Node CONS = new Node("CONS");
    public static final Node CAR = new Node("CAR");
    public static final Node CDR = new Node("CDR");
    public static final Node ATOM = new Node("ATOM");
    public static final Node QUOTE = new Node("QUOTE");
    public static final Node COND = new Node("COND");
    public static final Node DEFUN = new Node("DEFUN");

    private static final Map<String, Node> primitives;
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

    public static Map<String, Node> ids = new HashMap<>(primitives);


    public static Node cons(Node left, Node right) {
        Node result = new Node();
        result.left = left;
        result.right = right;
        return result;
    }

    public static Node addID(String id) {
        Node newNode = new Node(id);
        ids.put(id, newNode);
        return newNode;
    }
}
