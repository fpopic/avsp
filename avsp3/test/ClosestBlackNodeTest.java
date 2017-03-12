import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ClosestBlackNodeTest {

    private void run(String input, String expected, String actual) throws IOException {
        System.setOut(new PrintStream(new FileOutputStream(new File(actual)))); //stdout >> file
        ClosestBlackNode.main(new String[]{input}); //run

        Assert.assertEquals(
                Files.lines(Paths.get(expected)).collect(Collectors.joining(System.lineSeparator())),
                Files.lines(Paths.get(actual)).collect(Collectors.joining(System.lineSeparator()))
        );
    }

    @Test
    public void btest2() throws Exception {

        String input = "res/lab3B_primjeri/btest2/R.in";
        String expected = "res/lab3B_primjeri/btest2/R.out";
        String actual = expected + ".my";
        run(input, expected, actual);
    }

    @Test
    public void mtest2() throws Exception {

        String input = "res/lab3B_primjeri/mtest2/R.in";
        String expected = "res/lab3B_primjeri/mtest2/R.out";
        String actual = expected + ".my";
        run(input, expected, actual);
    }

    @Test
    public void stest2() throws Exception {

        String input = "res/lab3B_primjeri/stest2/R.in";
        String expected = "res/lab3B_primjeri/stest2/R.out";
        String actual = expected + ".my";
        run(input, expected, actual);
    }

    @Test
    public void ttest2() throws Exception {

        String input = "res/lab3B_primjeri/ttest2/R.in";
        String expected = "res/lab3B_primjeri/ttest2/R.out";
        String actual = expected + ".my";
        run(input, expected, actual);
    }


}