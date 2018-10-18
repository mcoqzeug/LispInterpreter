import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    String fileString;
    String nextFileString;
    static final int LEFT_PARENTHESIS = 0;
    static final int RIGHT_PARENTHESIS = 1;
    static final int DOT = 2;
//    static final int SPACE = 3;
    static final int IDENTIFIER = 4;
    static final int INTEGER = 5;
    static final int END_EXPRESSION = 6;
//    static final int END_FILE = 7;

    static final Pattern pattern = Pattern.compile("\\s*([(.)]|[^\\s(.)]*)(.*)");

    public Input(String filename) {
        this.fileString = readFile(filename);
        this.nextFileString = fileString;
    }

    public String readFile(String filename) {
        String fileString = "";
        try {
            FileInputStream fStream = new FileInputStream(filename);  // Open the file
            BufferedReader br = new BufferedReader(new InputStreamReader(fStream));

            StringBuilder fileStringBuilder = new StringBuilder();
            String line = "";

            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;

                fileStringBuilder.append(line);
            }

            fileString = fileStringBuilder.toString();
            fileString = fileString.trim().replaceAll(" +", " ");  // remove consecutive spaces

            br.close();  //Close the input stream

        } catch (FileNotFoundException ex) {
            System.err.println("A FileNotFoundException was caught: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("An IOException was caught: " + ex.getMessage());
        }

        return fileString;
    }

    public Node input() {
        String token = getNextToken();
        int tokenType = checkToken(token);
        if (tokenType == IDENTIFIER || tokenType == INTEGER) {
            skipToken();
            return getId(token);
        } else if (tokenType == LEFT_PARENTHESIS) {
            skipToken();
            Node left = input();
            int nextTokenType = checkToken(getNextToken());
            if (nextTokenType != DOT) {
                if (nextTokenType == IDENTIFIER || nextTokenType == INTEGER || nextTokenType == RIGHT_PARENTHESIS) {
                    // list notation
                    skipToken();
                    return inputList();
                }
                // error
                System.out.println("ERROR: syntax error.");
                return null;
            } else {
                skipToken();
                return Eval.cons(left, input());
            }
        }

        // in dot notation, a left parenthesis can only be followed by
        // another left parenthesis or an identifier. anything else would
        // be an error.
        System.out.println("ERROR: syntax error.");
        return null;
    }

    public Node inputList() {
        String token = getNextToken();
        int tokenType = checkToken(token);
        skipToken();
        if (tokenType == RIGHT_PARENTHESIS)
            return Eval.NIL;
        return Eval.cons(input(), inputList());
    }

    void skipToken() {
        fileString = nextFileString;
    }

    public String getNextToken() {
        Matcher matcher = pattern.matcher(fileString);
        if (matcher.find()) {
            nextFileString = matcher.group(2);
            return matcher.group(1);
        }
        return "";
    }

    public Node getId(String token) {
        if (Eval.ids.containsKey(token)) {
            return Eval.ids.get(token);
        }
        return Eval.addID(token);
    }

    public boolean checkValidity(String sExpressionStr) {
        return true;
    }

    public int checkToken(String token) {
        if (isInteger(token)) {
            return IDENTIFIER;
        }

        switch (token) {
            case "(":
                return LEFT_PARENTHESIS;
            case ")":
                return RIGHT_PARENTHESIS;
            case ".":
                return DOT;
            case "$":
                return END_EXPRESSION;
            default:
                return IDENTIFIER;
        }
    }

    public boolean isInteger(String str) {
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
