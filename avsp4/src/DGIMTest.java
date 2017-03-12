import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class DGIMTest {

    private void run(String input, String expected, String actual) throws IOException {
        System.setOut(new PrintStream(new FileOutputStream(new File(actual)))); //stdout >> file
        DGIM.main(new String[]{input}); //run

        Assert.assertEquals(
                Files.lines(Paths.get(expected)).collect(Collectors.joining(System.lineSeparator())),
                Files.lines(Paths.get(actual)).collect(Collectors.joining(System.lineSeparator()))
        );
    }

    @Test
    public void first() throws Exception {

        String input = "res/1.in";
        String expected = "res/1.out";
        String actual = expected + ".my";
        run(input, expected, actual);
    }

    @Test
    public void uputa() throws Exception {

        String input = "res/uputa.in";
        String expected = "res/uputa.out";
        String actual = expected + ".my";
        run(input, expected, actual);
    }

    @Test
    public void second() throws Exception {

        String input = "res/2.in";
        String expected = "res/2.out";
        String actual = expected + ".my";
        run(input, expected, actual);
    }

}