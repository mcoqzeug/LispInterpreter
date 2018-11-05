import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all the primitive atoms and
 * the implementation of primitive functions.It
 * also contains functions to evaluate a lisp
 * expression and print it using the Output class.
 *
 * @author  Juanxi Li
 * @version 1.0
 * @since   2018-10-24
 */

public class Eval {
    private static final int A_LIST = 0;
    private static final int D_LIST = 1;

    static final Node NIL = new Node("NIL");
    static final Node T = new Node("T");

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

    static final Map<String, Node> ids;
    static {
        ids = new HashMap<>();
        ids.put("NIL", NIL);
        ids.put("T", T);
        ids.put("PLUS", PLUS);
        ids.put("MINUS", MINUS);
        ids.put("LESS", LESS);
        ids.put("TIMES", TIMES);
        ids.put("GREATER", GREATER);
        ids.put("EQ", EQ);
        ids.put("CONS", CONS);
        ids.put("CAR", CAR);
        ids.put("CDR", CDR);
        ids.put("ATOM", ATOM);
        ids.put("QUOTE", QUOTE);
        ids.put("COND", COND);
        ids.put("DEFUN", DEFUN);
        ids.put("NULL", NULL);
        ids.put("INT", INT);
        ids.put("QUOTIENT", QUOTIENT);
        ids.put("REMAINDER", REMAINDER);
    }

    private static Node dList = NIL;  // An element in dList: (f . (pList . body))

    void interpret(Node sExpression) {
        if (Eval.car(sExpression) == DEFUN) System.out.println("> " + addToDList(sExpression));
        else {
            Node result = eval(sExpression, NIL);
            System.out.println("> " + Output.generateOutput(result));
        }
    }

    static Node cons(Node left, Node right) {
        return new Node(left, right);
    }

    static Node atom(Node node) {
        if (node.left == null && node.right == null) return T;
        return NIL;
    }

    private static Node null_fn(Node node) {
        if (node == NIL) return T;
        return NIL;
    }

    private static Node car(Node node) {
        if (atom(node) == T) throw new IllegalArgumentException("> ERROR: input to car cannot be an atom");
        return node.left;
    }

    private static Node cdr(Node node) {
        if (atom(node) == T) throw new IllegalArgumentException("> ERROR: input to cdr cannot be an atom");
        return node.right;
    }

    private static Node eq(Node node1, Node node2) {
        if (atom(node1) == T && atom(node2) == T) {
            if (node1.identifier != null && node2.identifier != null) { // identifiers
                if (node1 == node2) return T;
                return NIL;
            }
            if (node1.integer == node2.integer) return T;  // integers
            return NIL;
        }
        throw new IllegalArgumentException("> ERROR: input to eq should be atoms");
    }

    private static Node plus(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {  // should be 2 integers
            int result = node1.integer + node2.integer;
            return new Node(result);
        }
        throw new IllegalArgumentException("> ERROR: input to plus should be integers");
    }

    private static Node minus(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {  // should be 2 integers
            int result = node1.integer - node2.integer;
            return new Node(result);
        }
        throw new IllegalArgumentException("> ERROR: input to minus should be integers");
    }

    private static Node less(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {
            if (node1.integer < node2.integer) return T;
            return NIL;
        }
        throw new IllegalArgumentException("> ERROR: input to less should be integers");
    }

    private static Node times(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {
            int result = node1.integer * node2.integer;
            return new Node(result);
        }
        throw new IllegalArgumentException("> ERROR: input to times should be integers");
    }

    private static Node greater(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {
            if (node1.integer > node2.integer) return T;
            return NIL;
        }
        throw new IllegalArgumentException("> ERROR: input to greater should be integers");
    }

    private static Node int_fn(Node node) {
        if (atom(node) == T && node.identifier == null) return T;
        return NIL;
    }

    private static Node quotient(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {  // should be 2 integers
            int result = node1.integer / node2.integer;
            return new Node(result);
        }
        throw new IllegalArgumentException("> ERROR: input to quotient should be integers");
    }

    private static Node remainder(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {  // should be 2 integers
            int result = node1.integer % node2.integer;
            return new Node(result);
        }
        throw new IllegalArgumentException("> ERROR: input to remainder should be integers");
    }

    static Node addID(String id) {
        Node newNode = new Node(id);
        ids.put(id, newNode);
        return newNode;
    }

    private Node eval(Node exp, Node aList) {
        if (atom(exp) == T) {
            if (int_fn(exp) == T || exp == T || exp == NIL) return exp;
            return getVal(exp, aList, A_LIST);
        } else if (atom(car(exp)) == T) {
            if (eq(car(exp), QUOTE) == T) return car(cdr(exp));
            if (eq(car(exp), COND) == T) return evcon(cdr(exp), aList);

            // exp: (f . x) where x is a list of arguments
            return apply(car(exp), evlis(cdr(exp), aList), aList);
        }
        throw new IllegalArgumentException("> ERROR: Not a Lisp expression!");
    }

    private Node apply(Node f, Node x, Node aList) {
        if (atom(f) == T) {
            if (eq(f, CAR) == T) return car(car(x));
            if (eq(f, CDR) == T) return cdr(car(x));
            if (eq(f, CONS) == T) return cons(car(x), car(cdr(x)));
            if (eq(f, EQ) == T) return eq(car(x), car(cdr(x)));
            if (eq(f, ATOM) == T) return atom(car(x));
            if (eq(f, NULL) == T) return null_fn(car(x));
            if (eq(f, PLUS) == T) return plus(car(x), car(cdr(x)));
            if (eq(f, MINUS) == T) return minus(car(x), car(cdr(x)));
            if (eq(f, TIMES) == T) return times(car(x), car(cdr(x)));
            if (eq(f, QUOTIENT) == T) return quotient(car(x), car(cdr(x)));
            if (eq(f, REMAINDER) == T) return remainder(car(x), car(cdr(x)));
            if (eq(f, INT) == T) return int_fn(car(x));
            if (eq(f, GREATER) == T) return greater(car(x), car(cdr(x)));
            if (eq(f, LESS) == T) return less(car(x), car(cdr(x)));

            Node val = getVal(f, dList, D_LIST); // val: (pList . body)
            Node pList = car(val);
            return eval(cdr(val), addPairs(pList, x, aList));
        }
        throw new IllegalArgumentException("> ERROR: Not a Lisp expression!");
    }

    private Node evcon(Node be, Node aList) {
        if (null_fn(be) == T) throw new IllegalArgumentException("> ERROR: All conditions evaluated to false!");

        Node firstBE = car(be);
        if (eval(car(firstBE), aList) == T) return eval(car(cdr(firstBE)), aList);
        return evcon(cdr(be), aList);
    }

    private Node evlis(Node list, Node aList) {
        if (null_fn(list) == T) return NIL;
        return cons(eval(car(list), aList), evlis(cdr(list), aList));
    }

    private Node getVal(Node exp, Node list, int type) {
        // An element in list: (ai . bi)
        // Given an atom exp, find ai s.t. ai = exp and return bi

        if (atom(exp) == T) {
            if (null_fn(list) == T) {
                if (type == D_LIST)
                    throw new IllegalArgumentException("> ERROR: Function not defined");
                throw new IllegalArgumentException("> ERROR: Unbound variable");
            }

            Node pair = car(list);
            if (eq(exp, car(pair)) == T) return cdr(pair);
            return getVal(exp, cdr(list), type);
        }
        throw new IllegalArgumentException("> ERROR: The first argument of getVal should be an atom!");
    }

    private Node addPairs(Node pList, Node x, Node aList) {
        if (null_fn(pList) == T) {
            if (null_fn(x) == T) return aList;
            throw new IllegalArgumentException("> ERROR: Too many arguments");
        }
        if (null_fn(x) == T) throw new IllegalArgumentException("> ERROR: Too few arguments");
        return addPairs(cdr(pList), cdr(x), cons(cons(car(pList), car(x)), aList));
    }

    private String addToDList(Node exp) {
        // exp: (DEFUN (f pList) body)
        // An elements in dList: (f . (pList . body))

        exp = cdr(exp);  // remove DEFUN, exp = ((f pList) body)
        Node f_pList = car(exp);
        Node f = car(f_pList);
        if (atom(f) == NIL || f.identifier == null)
            throw new IllegalArgumentException("> ERROR: Invalid function name!");

        Node pList = car(cdr(f_pList));
        Node body = car(cdr(exp));
        dList = Eval.cons(Eval.cons(f, Eval.cons(pList, body)), dList);
        return f.identifier;
    }
}
