import java.io.*;
import java.util.ArrayList;

class Test {
    static void testOutput() {
        Node two = new Node(2);
        Node three = new Node(3);
        Node five = new Node(5);
        Node six = new Node(6);
        Node root = Eval.cons(Eval.cons(two, three), Eval.cons(five, six));
        System.out.println(Output.generateOutput(root));
    }

    static void testCorrectness() {
        String filename = "input.txt";
        Input input = new Input(filename);
        ArrayList<String> outputStrings = input.getOutputs();
        ArrayList<String> expectedOutputStrings = readFile("expected_output.txt");

        String out;
        String expected;

        for (int i=0; i<outputStrings.size(); i++) {
            out = outputStrings.get(i);
            out = out.replaceAll("\\s+","");
            expected = expectedOutputStrings.get(i);
            expected = expected.replaceAll("\\s+","");
            if (!out.equals(expected)) {
                System.out.println("Bad");
                System.out.println(out);
                System.out.println(expected);
                System.out.println();
            }
        }
    }

    private static ArrayList<String> readFile(String filename) {
        ArrayList<String> expectedOutputStrings = new ArrayList<>();
        String line;

        try {
            FileInputStream fStream = new FileInputStream(filename);  // Open the file
            BufferedReader br = new BufferedReader(new InputStreamReader(fStream));

            while ((line = br.readLine()) != null)
                expectedOutputStrings.add(line);

            br.close();  //Close the input stream

        } catch (FileNotFoundException ex) {
            System.err.println("A FileNotFoundException was caught: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("An IOException was caught: " + ex.getMessage());
        }

        return expectedOutputStrings;
    }
}
