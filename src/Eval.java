import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Eval {
    private String errorMsg = "";

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
    private static final Node NULL = new Node("NULL");
    private static final Node INT = new Node("INT");
    private static final Node QUOTIENT = new Node("QUOTIENT");
    private static final Node REMAINDER = new Node("REMAINDER");

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

    public static Node cons(Node left, Node right) {
        return new Node(left, right);
    }

    Node atom(Node node) {
        if (node.left == null && node.right == null)
            return T;
        return NIL;
    }

    Node null_fn(Node node) {
        if (node.equals(NIL)) {
            return T;
        }
        return NIL;
    }

    Node car(Node node) {
        if (atom(node).equals(T)) {
            errorMsg += "error: invalid input to car";
            return null;
        }
        return node.left;
    }

    Node cdr(Node node) {
        if (atom(node).equals(T)) {
            errorMsg += "error: invalid input to cdr";
            return null;
        }
        return node.right;
    }

    Node eq(Node node1, Node node2) {
        if (atom(node1).equals(T) && atom(node2).equals(T)) {
            if (node1.equals(node2))
                return T;
            return NIL;
        }

        errorMsg += "error: input to eq should be atoms";
        return null;
    }

    Node plus(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {  // should be 2 integers
            int result = node1.integer + node2.integer;
            return new Node(result);
        }

        errorMsg += "error: input to plus should be integers";
        return null;
    }

    Node minus(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {  // should be 2 integers
            int result = node1.integer - node2.integer;
            return new Node(result);
        }

        errorMsg += "error: input to minus should be integers";
        return null;
    }

    Node less(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {
            if (node1.integer < node2.integer)
                return T;
            return NIL;
        }

        errorMsg += "error: input to less should be integers";
        return null;
    }

    Node greater(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {
            if (node1.integer > node2.integer)
                return T;
            return NIL;
        }

        errorMsg += "error: input to greater should be integers";
        return null;
    }

    Node int_fn(Node node) {
        if (atom(node).equals(T) && node.identifier == null)
            return T;
        return NIL;
    }

    Node quotient(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {  // should be 2 integers
            int result = node1.integer / node2.integer;
            return new Node(result);
        }

        errorMsg += "error: input to quotient should be integers";
        return null;
    }

    Node remainder(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {  // should be 2 integers
            int result = node1.integer % node2.integer;
            return new Node(result);
        }

        errorMsg += "error: input to remainder should be integers";
        return null;
    }

    Node addID(String id) {
        Node newNode = new Node(id);
        ids.put(id, newNode);  // TODO
        return newNode;
    }

    Node eval(Node exp, Node aList, Node dList) {
        if (atom(exp).equals(T)) {
            if (int_fn(exp).equals(T) || exp.equals(T) || exp.equals(NIL))
                return exp;

            if (isIn(exp, aList))
                return getVal(exp, aList);

            errorMsg += "error: unbound variable";
            return null;
        } else if (atom(car(exp)).equals(T)) {
            if (car(exp).equals(QUOTE))
                return car(cdr(exp));

            if (car(exp).equals(COND))
                return evcon(cdr(exp), aList, dList);

            if (car(exp).equals(DEFUN)) {
                // TODO add to dList (state change!)
            } else {
                return apply(car(exp), evlis(cdr(exp), aList, dList), aList, dList);
            }
        }

        errorMsg += "error: ";  // TODO
        return null;
    }

    Node apply(Node f, Node x, Node aList, Node dList) {
        if (atom(f).equals(T)) {
            if (f.equals(CAR)) {

            }
        }
        return null;
    }

    Node evcon(Node exp, Node aList, Node dList) {
        return null;
    }

    Node evlis(Node exp, Node aList, Node dList) {
        return null;
    }

    boolean isIn(Node exp, Node aList) {
        // TODO
        return true;
    }

    Node getVal(Node exp, Node aList) {
        // TODO
        return null;
    }
}
