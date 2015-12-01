package loa;

/** Utility for reporting informational or debugging messages conditionally,
 *  based on an adjustable parameter.
 *  @author P. N. Hilfinger
 */
public class Reporter {

    /** Set the message level for this package to LEVEL.  The debug() routine
     *  (below) will print any message with a positive level that is <= LEVEL.
     *  Initially, the level is 0. */
    public static void setMessageLevel(int level) {
        _messageLevel = level;
    }

    /** Returns the current message level, as set by setMessageLevel. */
    public static int getMessageLevel() {
        return _messageLevel;
    }

    /** Print a message on the standard error if LEVEL is positive and <= the
     *  current message level. FORMAT and ARGS are as for the .printf
     *  methods. */
    public static void debug(int level, String format, Object... args) {
        if (level > 0 && level <= _messageLevel) {
            System.err.printf(format, args);
            System.err.println();
        }
    }

    /** The current package-wide message level. */
    private static int _messageLevel = 0;

}
