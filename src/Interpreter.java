import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Interpreter {

    public static void main(String[] args) {
        Input input = new Input();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            StringBuilder sExpStrBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;

                if (line.equals("$")) {
                    input.buildTreeAndPrint(sExpStrBuilder);
                    sExpStrBuilder = new StringBuilder();
                    continue;
                }

                if (line.equals("$$")) {
                    input.buildTreeAndPrint(sExpStrBuilder);
                    break;
                }

                line = line.trim().replaceAll("\\s+", " ");  // remove consecutive spaces
                line = line + " ";  // replace newline with space
                sExpStrBuilder.append(line);
            }

            br.close();

        } catch (IOException ex) {
            System.err.println("An IOException was caught: " + ex.getMessage());
        }
    }
}
