package GraphDecomposition;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by lkuligin on 30/12/2016.
 */
public class ConnectedComponentsTest {
        private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        @Before
        public void redirectOutputStream() {
            System.setOut(new PrintStream(outContent));
        }

        @Test
        public void TestSmallGraph() {
            String inputString = "1 1\n"
                    + "1 1\n"
                    + "1 1\n";
            ByteArrayInputStream input = new ByteArrayInputStream(inputString.getBytes());
        /*System.setIn(input);*/

            int expected = 1;

            ConnectedComponents r = new ConnectedComponents();
            System.setIn(input);
            r.main(null);

            String results = outContent.toString();

            assertEquals(expected, Integer.parseInt(results.trim()));

            System.setIn(System.in);
        }

        @Test
        public void TestExampleGraph() {
            String inputString = "4 2\n"
                    + "1 2\n"
                    + "3 2\n";
            ByteArrayInputStream input = new ByteArrayInputStream(inputString.getBytes());

            int expected = 2;

            ConnectedComponents r = new ConnectedComponents();
            System.setIn(input);
            r.main(null);

            String results = outContent.toString();

            assertEquals(expected, Integer.parseInt(results.trim()));

            System.setIn(System.in);
        }

        @Test
        public void TestNonExistingPath() {
            String inputString = "3 0\n";
            ByteArrayInputStream input = new ByteArrayInputStream(inputString.getBytes());
        /*System.setIn(input);*/

            int expected = 3;

            ConnectedComponents r = new ConnectedComponents();
            System.setIn(input);
            r.main(null);

            String results = outContent.toString();

            assertEquals(expected, Integer.parseInt(results.trim()));

            System.setIn(System.in);
        }

        @After
        public void cleanUpOutputStream() {
            System.setOut(null);
        }
    }
