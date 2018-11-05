import java.util.HashMap;
import java.util.Map;

public class Eval {
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
        primitives.put("NULL", NULL);
        primitives.put("INT", INT);
        primitives.put("QUOTIENT", QUOTIENT);
        primitives.put("REMAINDER", REMAINDER);
    }

    static Map<String, Node> ids = new HashMap<>(primitives);

    private static Node dList = NIL;  // Elements on dList: (f . (pList . body))

    void interpret(Node sExpression) {
        if (Eval.car(sExpression) == DEFUN) {
            System.out.println("> " + addToDList(sExpression));
        } else {
            Node result = eval(sExpression, NIL);
            System.out.println("> " + Output.generateOutput(result));
        }
    }

    static Node cons(Node left, Node right) {
        return new Node(left, right);
    }

    private static Node atom(Node node) {
        if (node.left == null && node.right == null) return T;
        return NIL;
    }

    private static Node null_fn(Node node) {
        if (node == NIL) return T;
        return NIL;
    }

    private static Node car(Node node) {
        if (atom(node) == T) throw new IllegalArgumentException("ERROR: input to car cannot be an atom");
        return node.left;
    }

    private static Node cdr(Node node) {
        if (atom(node) == T) throw new IllegalArgumentException("ERROR: input to cdr cannot be an atom");
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
        throw new IllegalArgumentException("ERROR: input to eq should be atoms");
    }

    private static Node plus(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {  // should be 2 integers
            int result = node1.integer + node2.integer;
            return new Node(result);
        }
        throw new IllegalArgumentException("ERROR: input to plus should be integers");
    }

    private static Node minus(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {  // should be 2 integers
            int result = node1.integer - node2.integer;
            return new Node(result);
        }
        throw new IllegalArgumentException("ERROR: input to minus should be integers");
    }

    private static Node less(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {
            if (node1.integer < node2.integer) return T;
            return NIL;
        }
        throw new IllegalArgumentException("ERROR: input to less should be integers");
    }

    private static Node times(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {
            int result = node1.integer * node2.integer;
            return new Node(result);
        }
        throw new IllegalArgumentException("ERROR: input to times should be integers");
    }

    private static Node greater(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {
            if (node1.integer > node2.integer) return T;
            return NIL;
        }
        throw new IllegalArgumentException("ERROR: input to greater should be integers");
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
        throw new IllegalArgumentException("ERROR: input to quotient should be integers");
    }

    private static Node remainder(Node node1, Node node2) {
        if (int_fn(node1) == T && int_fn(node2) == T) {  // should be 2 integers
            int result = node1.integer % node2.integer;
            return new Node(result);
        }
        throw new IllegalArgumentException("ERROR: input to remainder should be integers");
    }

    static Node addID(String id) {
        Node newNode = new Node(id);
        ids.put(id, newNode);
        return newNode;
    }

    private Node eval(Node exp, Node aList) {
        if (atom(exp) == T) {
            if (int_fn(exp) == T || exp == T || exp == NIL) return exp;
            if (isIn(exp, aList) == T) return getVal(exp, aList);  // todo
            throw new IllegalArgumentException("ERROR: unbound variable");
        } else if (atom(car(exp)) == T) {
            if (eq(car(exp), QUOTE) == T) return car(cdr(exp));
            if (eq(car(exp), COND) == T) return evcon(cdr(exp), aList);

            // exp: (f . x) where x is a list of arguments
            return apply(car(exp), evlis(cdr(exp), aList), aList);
        }
        throw new IllegalArgumentException("ERROR: Not a Lisp expression!");
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

            Node val = getVal(f, dList); // val: (pList . body)
            Node pList = car(val);
            return eval(cdr(val), addPairs(pList, x, aList));
        }
        throw new IllegalArgumentException("ERROR: Not a Lisp expression!");
    }

    private Node evcon(Node be, Node aList) {
        if (null_fn(be) == T) throw new IllegalArgumentException("ERROR: All conditions evaluated to false!");

        Node firstBE = car(be);
        if (eval(car(firstBE), aList) == T) return eval(car(cdr(firstBE)), aList);
        return evcon(cdr(be), aList);
    }

    private Node evlis(Node list, Node aList) {
        if (null_fn(list) == T) return NIL;
        return cons(eval(car(list), aList), evlis(cdr(list), aList));
    }

    private static Node isIn(Node s, Node list) {
        // check if atom s is in list
        // an element in list: (ai . bi)
        // given s, find ai s.t. s = ai, if such ai exists, return T, else return NIL

        if (atom(s) == T) {
            if (null_fn(list) == T) return NIL;
            if (eq(s, car(car(list))) == T) return T;
            return isIn(s, cdr(list));
        }
        throw new IllegalArgumentException("ERROR: The first argument to isIn should be an atom!");
    }

    private Node getVal(Node exp, Node list) {
        // An element in list: (ai . bi)
        // Given an atom exp, find ai s.t. ai = exp and return bi

        if (null_fn(list) == T) throw new IllegalArgumentException("ERROR: Cannot find corresponding values");

        Node pair = car(list);
        if (eq(car(pair), exp) == T) return cdr(pair);
        return getVal(exp, cdr(list));
    }

    private Node addPairs(Node pList, Node x, Node aList) {
        // An element in aList: (pi . aiv)

        if (null_fn(pList) == T) {
            if (null_fn(x) == T) return aList;
            throw new IllegalArgumentException("ERROR: Too many arguments");
        }
        if (null_fn(x) == T) throw new IllegalArgumentException("ERROR: Too few arguments");
        return addPairs(cdr(pList), cdr(x), cons(cons(car(pList), car(x)), aList));
    }

    private String addToDList(Node exp) {
        // exp: (DEFUN (f pList) body)
        // An elements in dList: (f . (pList . body))

        exp = cdr(exp);  // remove DEFUN, exp = ((f pList) body)
        Node f_pList = car(exp);
        Node f = car(f_pList);
        if (atom(f) == NIL || f.identifier == null)
            throw new IllegalArgumentException("ERROR: Invalid function name!");

        Node pList = car(cdr(f_pList));
        Node body = car(cdr(exp));
        dList = Eval.cons(Eval.cons(f, Eval.cons(pList, body)), dList);
        return f.identifier;
    }
}
