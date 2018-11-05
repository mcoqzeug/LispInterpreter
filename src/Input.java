import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class converts a string of s-expression to an instance of Node class.
 *
 * @author  Juanxi Li
 * @version 1.0
 * @since   2018-10-24
 */

class Input {
    private String sExpressionString;
    private String nextSExpressionString;

    private static final int LEFT_PARENTHESIS = 0;
    private static final int RIGHT_PARENTHESIS = 1;
    private static final int DOT = 2;
    private static final int IDENTIFIER = 3;
    private static final int INTEGER = 4;

    private static final Pattern pattern = Pattern.compile("\\s*([(.)]|[^\\s(.)]*)(.*)");

    Node getNode(String str) {  // constructor?
        sExpressionString = str;
        if (sExpressionString.isEmpty()) throw new IllegalArgumentException("> ERROR: illegal s-expression");
        Node sExpression = input();
        if (!sExpressionString.isEmpty()) throw new IllegalArgumentException("> ERROR: illegal s-expression");
        return sExpression;
    }

    private Node input() {
        String token = getToken();
        int tokenType = getTokenType(token);

        if (tokenType == INTEGER) {
            skipToken();
            return getInt(token);
        }

        if (tokenType == IDENTIFIER ) {
            skipToken();
            return getId(token);
        }

        if (tokenType == LEFT_PARENTHESIS) {
            skipToken();  // skip "("

            if (getTokenType(getToken()) == RIGHT_PARENTHESIS) {
                skipToken();
                return Eval.NIL;
            }

            Node left = input();

            int nextTokenType = getTokenType(getToken());

            if (nextTokenType != DOT) return Eval.cons(left, inputList());  // list notation

            skipToken();  // skip dot
            Node right = input();

            // the input() above should bring us to the final ")"
            token = getToken();
            nextTokenType = getTokenType(token);
            if (nextTokenType != RIGHT_PARENTHESIS)
                throw new IllegalArgumentException(String.format("> ERROR: unexpected %s", token));

            skipToken();  // skip the final ")"
            return Eval.cons(left, right);  // this should return to the original call of input
                                            // if the current sExpression is legal
        }

        // In dot notation, a "(" can only be followed by
        // another "(" or an identifier. Anything else would
        // be an error.
        throw new IllegalArgumentException(String.format("> ERROR: unexpected %s", token));
    }

    private Node inputList() {
        // When inputList() is called,
        // some input() must have seen a "(".

        String token = getToken();
        int tokenType = getTokenType(token);

        if (tokenType == RIGHT_PARENTHESIS) {
            skipToken();
            return Eval.NIL;
        }

        // tokenType = INTEGER, IDENTIFIER, or LEFT_PARENTHESIS, don't skip, let input() do the skipping
        Node left = input();
        Node right = inputList();
        return Eval.cons(left, right);  // Would either return to this line or
    }

    private void skipToken() {
        sExpressionString = nextSExpressionString;
        // nextSExpressionString is updated when getToken is called
    }

    private String getToken() {
        Matcher matcher = pattern.matcher(sExpressionString);
        if (matcher.find()) {
            nextSExpressionString = matcher.group(2);
            return matcher.group(1);
        }
        throw new IllegalArgumentException("> ERROR: Cannot find token!");
    }

    private int getTokenType(String token) {
        if (isInteger(token)) return INTEGER;

        switch (token) {
            case "(":
                return LEFT_PARENTHESIS;
            case ")":
                return RIGHT_PARENTHESIS;
            case ".":
                return DOT;
            default:
                return IDENTIFIER;
        }
    }

    private Node getId(String token) {
        if (!isIdValid(token)) throw new IllegalArgumentException("> ERROR: invalid token");
        if (Eval.ids.containsKey(token)) return Eval.ids.get(token);
        return Eval.addID(token);
    }

    private Node getInt(String token) {
        int integer = Integer.parseInt(token);
        return new Node(integer);
    }

    private static boolean isIdValid(String token) {
        char c = token.charAt(0);

        if (c < 'A' || c > 'Z') return false;

        for (int i=1; i<token.length(); i++) {
            c = token.charAt(i);
            if ((c < 'A' || c > 'Z') && (c < '0' || c > '9')) return false;
        }
        return true;
    }

    private static boolean isInteger(String str) {
        if (str.isEmpty()) return false;

        int i = 0, length = str.length();
        char c = str.charAt(0);

        if (c == '-' || c == '+') {
            if (length == 1) return false;
            i = 1;
        }

        for (; i < length; i++) {
            c = str.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }
}
