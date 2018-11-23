/*
 * Author Vytautas Ambrasiunas 2018.
 */

package tests;


import ambrasiunas.Application;
import ambrasiunas.domain.shapes.AnyShape;
import ambrasiunas.services.ShapeService;
import ambrasiunas.services.ShapesServiceImpl;
import ambrasiunas.utils.MenuUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThat;

public class TestMenuInput {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(System.in);
    }

    @Test()
    public void testHelp() {
        setInputCommand("help");
        initApplication();
        assertThat(outContent.toString(), CoreMatchers.containsString(MenuUtils.getHelpText()));
    }

    @Test
    public void testFile() {
        setInputCommand("file");
        initApplication();
        assertThat(outContent.toString(), CoreMatchers.containsString("Loading shapes from file....."));

    }

    @Test
    public void testList() {
        setInputCommand("file");
        setInputCommand("list");
        initApplication();
        assertThat(outContent.toString(), CoreMatchers.containsString(getAllShapeStringIdentifier()));
    }

    @Test
    public void testCreateShape() {
        setInputCommand("circle 4 3 1");
        initApplication();
        assertThat(outContent.toString(), CoreMatchers.containsString(getAllShapeStringIdentifier()));

    }

    private void initApplication() {
        Application testApplication = new Application(new ShapesServiceImpl());
        testApplication.initConsoleMenu();
    }

    private void setInputCommand(String command) {
        ByteArrayInputStream in = new ByteArrayInputStream(command.getBytes());
        System.setIn(in);
    }

    private List<String> outToLines() {
        String lines[] = outContent.toString().split("\\r?\\n");
        List<String> outPutLines = Arrays.asList(lines);
        return outPutLines;
    }

    private String getAllShapeStringIdentifier() {
        String resultString = "";
        ShapeService shapeService = new ShapesServiceImpl();
        for (AnyShape anyShape : shapeService.getAllShapes()) {
            resultString += anyShape.getIdentifier();
        }
        return resultString;
    }
}
