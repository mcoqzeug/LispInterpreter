import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class takes in strings from a file or standard input
 * and interpret them.
 *
 * @author  Juanxi Li
 * @version 1.0
 * @since   2018-10-24
 */

public class Interpreter {
    public static void main(String[] args) {
        Input input = new Input();
        Eval evaluator = new Eval();
        BufferedReader br;

        try {
            if (args.length > 0) {  // input from file
                FileReader fr = new FileReader(args[0]);
                br = new BufferedReader(fr);
            } else {  // input from stream
                br = new BufferedReader(new InputStreamReader(System.in));
            }
            StringBuilder sExpStrBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.equals("")) continue;
                if (line.equals("$") || line.equals("$$")) {
                    try {
                        String sExpressionString = sExpStrBuilder.toString();
                        sExpressionString = sExpressionString
                                .trim().replaceAll("\\s+", " ");  // remove consecutive spaces

                        Node sExpression = input.getNode(sExpressionString);
                        evaluator.interpret(sExpression);
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }

                    if (line.equals("$")) {
                        sExpStrBuilder = new StringBuilder();
                        continue;
                    }
                    break;
                }
                line = line + " ";  // replace newline with space
                sExpStrBuilder.append(line);
            }
            br.close();
        } catch (IOException ex) {
            System.err.println("An IOException was caught: " + ex.getMessage());
        }
    }
}
