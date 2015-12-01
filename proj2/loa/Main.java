package loa;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import ucb.util.CommandArgs;

import static loa.Piece.*;

/** Main class of the Lines of Action program.
 * @author Yuki Mizuno
 */
public class Main {

    /** Version designator. */
    public static final String VERSION = "1.0";

    /** The main Lines of Action. Possible ARGS are
     *      --display       Use a GUI.
     *      --debug=LEVEL   Turn on debugging output.  Print debugging
     *                      meesages up to level LEVEL.  0 indicates no
     *                      debugging messages (the default).  (This option
     *                      is not part of the spec.)
     */
    public static void main(String... args) {
        CommandArgs options =
            new CommandArgs("--debug=(\\d+){0,1} --display{0,1}", args);

        if (!options.ok()) {
            usage();
        }
        System.out.printf("Lines of Action.  Version %s.%nType ? for help.%n",
                          VERSION);

        if (options.contains("--display")) {
            error(1, "--display not supported.");
        }

        if (options.contains("--debug")) {
            Reporter.setMessageLevel(options.getInt("--debug"));
        }

        Game game = new Game();
        game.play();
    }

    /** Print brief description of the command-line format. */
    static void usage() {
        printResource("loa/usage.txt");
    }

    /** Print the contents of the resource named NAME on the standard error.
     *  The resource can be any file in the class directory.  File
     *  loa/foo.txt, for example, is named simply "loa/foo.txt". */
    static void printResource(String name) {
        try {
            InputStream resourceStream =
                Main.class.getClassLoader().getResourceAsStream(name);
            BufferedReader str =
                new BufferedReader(new InputStreamReader(resourceStream));
            for (String s = str.readLine(); s != null; s = str.readLine())  {
                System.err.println(s);
            }
            str.close();
        } catch (IOException excp) {
            System.out.println("No help found.");
        }
    }

    /** Report an error and exit program with EXIT as the
     *  exit code if _strict is false; otherwise exit with code 2.
     *  FORMAT is the message format (as for printf), and ARGS any
     *  additional arguments. */
    static void error(int exit, String format, Object... args) {
        error(format, args);
        System.exit(exit);
    }

    /** Report an error.  If _strict, then exit (code 2).  Otherwise,
     *  simply return. FORMAT is the message format (as for printf),
     *  and ARGS any additional arguments. */
    static void error(String format, Object... args) {
        System.err.print("Error: ");
        System.err.printf(format, args);
    }

}
