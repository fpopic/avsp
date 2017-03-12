import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class SimHashTest {

    private void run(String input, String expected, String actual) throws IOException {
        System.setOut(new PrintStream(new FileOutputStream(new File(actual)))); //stdout >> file
        SimHash.main(new String[]{input}); //run PCY

        Assert.assertEquals(
                Files.lines(Paths.get(expected)).collect(Collectors.joining(System.lineSeparator())),
                Files.lines(Paths.get(actual)).collect(Collectors.joining(System.lineSeparator()))
        );
    }

    @Test
    public void main() throws Exception {

        String input = "res/1a/test2/R.in";
        String expected = "res/1a/test2/R.out";
        String actual = expected + ".my";
        run(input, expected, actual);
    }
}