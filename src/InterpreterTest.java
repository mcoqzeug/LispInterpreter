import org.junit.Assert;
import org.junit.Test;


public class InterpreterTest {
    @Test
    public void testOutput() {
        Node two = new Node(2);
        Node three = new Node(3);
        Node five = new Node(5);
        Node six = new Node(6);
        Node root = Eval.cons(Eval.cons(two, three), Eval.cons(five, six));
        System.out.println(Output.generateOutput(root));
    }

    @Test
    public void equals() {
        Node two = new Node(2);
        Node three = new Node(3);
        Node two1 = new Node(2);
        Assert.assertNotEquals(two, two1);
        Assert.assertNotEquals(two, three);
    }
}
