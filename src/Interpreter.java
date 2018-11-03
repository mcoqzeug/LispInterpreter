import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Interpreter {

    public static void main(String[] args) {
        Input input = new Input();

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
                if (line.equals(""))
                    continue;

                if (line.equals("$") || line.equals("$$")) {
                    try {
                        input.buildTreeAndPrint(sExpStrBuilder);
                    } catch (IllegalArgumentException ex) {
                        System.err.println(ex.getMessage());
                    }

//                    try {
//                        Thread.sleep(5);
//                    }
//                    catch(InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }

                    if (line.equals("$")) {
                        sExpStrBuilder = new StringBuilder();
                        continue;
                    } else {
                        break;
                    }

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
