import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Eval {

    static final Node NIL = new Node("NIL");
    static final Node T = new Node("T");

    static final Node PLUS = new Node("PLUS");
    static final Node MINUS = new Node("MINUS");
    static final Node LESS = new Node("LESS");
    static final Node TIMES = new Node("TIMES");
    static final Node GREATER = new Node("GREATER");
    static final Node EQ = new Node("EQ");
    static final Node CONS = new Node("CONS");
    static final Node CAR = new Node("CAR");
    static final Node CDR = new Node("CDR");
    static final Node ATOM = new Node("ATOM");
    static final Node QUOTE = new Node("QUOTE");
    static final Node COND = new Node("COND");
    static final Node DEFUN = new Node("DEFUN");
    static final Node NULL = new Node("NULL");
    static final Node INT = new Node("INT");
    static final Node QUOTIENT = new Node("QUOTIENT");
    static final Node REMAINDER = new Node("REMAINDER");

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

    Map<String, Node> ids = new HashMap<>(primitives);

    static Node cons(Node left, Node right) {
        return new Node(left, right);
    }

    static Node atom(Node node) {
        if (node.left == null && node.right == null)
            return T;
        return NIL;
    }

    static Node null_fn(Node node) {
        if (node.equals(NIL)) {
            return T;
        }
        return NIL;
    }

    static Node car(Node node) {
        if (atom(node).equals(T))
            throw new IllegalArgumentException("ERROR: input to car cannot be an atom");
        return node.left;
    }

    static Node cdr(Node node) {
        if (atom(node).equals(T))
            throw new IllegalArgumentException("ERROR: input to cdr cannot be an atom");
        return node.right;
    }

    static Node eq(Node node1, Node node2) {
        if (atom(node1).equals(T) && atom(node2).equals(T)) {
            if (node1.equals(node2))
                return T;
            return NIL;
        }

        throw new IllegalArgumentException("ERROR: input to eq should be atoms");
    }

    static Node plus(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {  // should be 2 integers
            int result = node1.integer + node2.integer;
            return new Node(result);
        }

        throw new IllegalArgumentException("ERROR: input to plus should be integers");
    }

    static Node minus(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {  // should be 2 integers
            int result = node1.integer - node2.integer;
            return new Node(result);
        }

        throw new IllegalArgumentException("ERROR: input to minus should be integers");
    }

    static Node less(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {
            if (node1.integer < node2.integer)
                return T;
            return NIL;
        }

        throw new IllegalArgumentException("ERROR: input to less should be integers");
    }

    static Node greater(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {
            if (node1.integer > node2.integer)
                return T;
            return NIL;
        }

        throw new IllegalArgumentException("ERROR: input to greater should be integers");
    }

    static Node int_fn(Node node) {
        if (atom(node).equals(T) && node.identifier == null)
            return T;
        return NIL;
    }

    static Node quotient(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {  // should be 2 integers
            int result = node1.integer / node2.integer;
            return new Node(result);
        }

        throw new IllegalArgumentException("ERROR: input to quotient should be integers");
    }

    static Node remainder(Node node1, Node node2) {
        if (int_fn(node1).equals(T) && int_fn(node2).equals(T)) {  // should be 2 integers
            int result = node1.integer % node2.integer;
            return new Node(result);
        }

        throw new IllegalArgumentException("ERROR: input to remainder should be integers");
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

             throw new IllegalArgumentException("error: unbound variable");
        } else if (atom(car(exp)).equals(T)) {
            if (car(exp).equals(QUOTE))
                return car(cdr(exp));

            if (car(exp).equals(COND))
                return evcon(cdr(exp), aList, dList);

            if (car(exp).equals(DEFUN)) {
                // TODO add to dList (state change!) should probably do this before calling eval
            } else {
                return apply(car(exp), evlis(cdr(exp), aList, dList), aList, dList);
            }
        }

        throw new IllegalArgumentException("error: ");  // TODO
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
