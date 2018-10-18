import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Input {
    private String sExpressionString;
    private String nextSExpressionString;
    private ArrayList<String> sExpressions = new ArrayList<>();
    private int sExpressionCount = 0;

    private static final int LEFT_PARENTHESIS = 0;
    private static final int RIGHT_PARENTHESIS = 1;
    private static final int DOT = 2;
    private static final int IDENTIFIER = 3;
    private static final int INTEGER = 4;

    private static final Pattern pattern = Pattern.compile("\\s*([(.)]|[^\\s(.)]*)(.*)");

    Input(String filename) {
        readFile(filename);
    }

    private void readFile(String filename) {
        try {
            FileInputStream fStream = new FileInputStream(filename);  // Open the file
            BufferedReader br = new BufferedReader(new InputStreamReader(fStream));

            StringBuilder fileStringBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;

                if (line.equals("$") || line.equals("$$")) {

                    // initialize sExpressionString
                    String sExpressionString = fileStringBuilder.toString();
                    sExpressionString = sExpressionString.trim().replaceAll(" +", " ");  // remove consecutive spaces

                    Node sExpression = input();

                    if (sExpression.errorMsg == null) {
                        System.out.println(Output.generateOutput(sExpression));
                    } else {
                        System.out.println(sExpression.errorMsg);
                    }

                    fileStringBuilder = new StringBuilder();

                    if (line.equals("$$"))
                        break;
                }
                else {
                    line = " " + line;
                    fileStringBuilder.append(line);
                }
            }

            br.close();  //Close the input stream

        } catch (FileNotFoundException ex) {
            System.err.println("A FileNotFoundException was caught: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("An IOException was caught: " + ex.getMessage());
        }
    }

//    ArrayList<String> getOutputs() {
//        ArrayList<String> outputStrings = new ArrayList<>();
//        String outputStr;
//
//        while (sExpressionCount < sExpressions.size()) {
//            sExpressionString = sExpressions.get(sExpressionCount);
//
//            if (getTokenType(getToken()) == LEFT_PARENTHESIS) {
//                Node sExpression = input();
//                if (sExpression != null) {  // there's no error
//                    outputStr = Output.generateOutput(sExpression);
//                    outputStrings.add(outputStr);
//                } else {
//                    outputStrings.add("some error");
//                }
//            } else {
//                System.out.println("ERROR: s-expression should start with \"(\"");
//            }
//
//            sExpressionCount++;
//        }
//
//        return outputStrings;
//    }

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

            Node left = input();

            if (left.errorMsg != null)
                return left;

            int nextTokenType = getTokenType(getToken());

            if (nextTokenType != DOT) {  // list notation
                return Eval.cons(left, inputList());
            }

            skipToken();  // skip dot
            Node right = input();

            if (right.errorMsg != null)
                return right;

            // the input() above should bring us to the final ")"
            nextTokenType = getTokenType(getToken());
            if (nextTokenType != RIGHT_PARENTHESIS) {
                Node node = new Node();
                node.errorMsg = "ERROR: Expect \")\"";
                return node;
            }

            skipToken();  // skip the final ")"
            return Eval.cons(left, right);  // this should return to the original call of input
                                            // if the current sExpression is legal
        }

        // In dot notation, a "(" can only be followed by
        // another "(" or an identifier. Anything else would
        // be an error.
        Node node = new Node();
        node.errorMsg = String.format("ERROR: Token %s appears in the wrong place.", token);
        return node;
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

        if (left.errorMsg != null)
            return left;

        Node right = inputList();

        if (right.errorMsg != null)
            return right;

        return Eval.cons(left, right);  // Would either return to this line or
    }

    private void skipToken() {
        sExpressionString = nextSExpressionString;
//        getToken();
    }

    private String getToken() {
        Matcher matcher = pattern.matcher(sExpressionString);
        if (matcher.find()) {
            nextSExpressionString = matcher.group(2);
            return matcher.group(1);
        }
        return "";
    }

    private int getTokenType(String token) {
        if (isInteger(token)) {
            return INTEGER;
        }

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
        if (!isIdValid(token)) {
            Node node = new Node();
            node.errorMsg = String.format("ERROR: invalid character in identifier %s.", token);
            return node;
        }

        if (Eval.ids.containsKey(token))
            return Eval.ids.get(token);

        return Eval.addID(token);
    }

    private Node getInt(String token) {
        int integer = Integer.parseInt(token);
        return new Node(integer);
    }

    private boolean isIdValid(String token) {
        char c = token.charAt(0);

        if (c < 'A' || c > 'Z') {
            return false;
        }

        for (int i=1; i<token.length(); i++) {
            c = token.charAt(i);
            if ((c < 'A' || c > 'Z') && (c < '0' || c > '9')) {
                return false;
            }
        }

        return true;
    }

    private boolean isInteger(String str) {
        int length = str.length();

        if (str.isEmpty()) {
            return false;
        }

        int i = 0;

        if (str.charAt(0) == '-') {
            if (length == 1)
                return false;
            i = 1;
        }

        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }

}
