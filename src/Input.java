import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {
    public Input(String filename) {
        ArrayList<String> sExpressionList = new ArrayList<>();
        sExpressionList = readFile(filename);
        System.out.println(sExpressionList);
    }

    /*
    * Read File Line By Line and convert them to a list of SExpressions.
    * */
    private ArrayList readFile(String filename) {
        ArrayList<String> sExpressionList = new ArrayList<>();

        try {
            FileInputStream fStream = new FileInputStream(filename);  // Open the file
            BufferedReader br = new BufferedReader(new InputStreamReader(fStream));

            StringBuilder sExpression = new StringBuilder();
            String sExpressionStr;

            for (String nextLine, line = br.readLine(); line != null; line = nextLine) {
                nextLine = br.readLine();

                // process the input line
                if (line.equals("$") || line.equals("$$")) {
                    sExpressionStr = sExpression.toString();
                    sExpressionList.add(sExpressionStr);
                    sExpression = new StringBuilder();  // reset to empty
                } else {
                    sExpression.append(line);
                }

                // check if "$$" is at the end of the input file
                if (line.equals("$$")) {
                    if (nextLine != null)
                        System.out.println("ERROR: \"$$\" is not at the end.");
                    break;
                } else {
                    if (nextLine == null)
                        System.out.println("ERROR: \"$$\" is not at the end.");
                }
            }

            br.close();  //Close the input stream

        } catch (FileNotFoundException ex) {
            System.err.println("A FileNotFoundException was caught: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("An IOException was caught: " + ex.getMessage());
        }

        return sExpressionList;
    }

    private boolean checkValidity(String sExpressionStr) {
//        StringTokenizer tokenizer = new StringTokenizer(sExpressionStr);
//        String[] tokens = sExpressionStr.trim().split("\\s+");
        Pattern pattern = Pattern.compile("\"\\s*(,@|[('`,)]|\"(?:[\\\\].|[^\\\\\"])*\"|;.*|[^\\s('\"`,;)]*)(.*)\"");
        Matcher matcher = pattern.matcher(sExpressionStr);
        System.out.println(matcher.group());
        // sExpressionStr = sExpressionStr.replaceAll("\\s+", " ");  // remove consecutive spaces
        boolean valid = true;

        return valid;
    }

    private String getNextToken(String line) {
        return "";
    }

//    private buildTree() {
//        String token = getNextToken();
//    }

}
