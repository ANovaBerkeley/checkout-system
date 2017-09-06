package loa;

/** A direction of motion on a LOA board.
 *  @author P. N. Hilfinger
 */
enum Direction {

    /** Sentinel value. */
    NOWHERE(0, 0),
    /** Compass directions. */
        N(0, 1), NE(1, 1), E(1, 0), SE(1, -1), S(0, -1),
    /** Compass directions. */
        SW(-1, -1), W(-1, 0), NW(-1, 1);

    /** Return a Direction in which each step increments the column
     *  by DC0 and the row by DR0. */
    Direction(int dc0, int dr0) {
        dc = dc0;
        dr = dr0;
    }

    /** Return the next direction clockwise.  NOWHERE's successor by
     *  convention is N, and the successor of NW is null. */
    Direction succ() {
        return (this == NW) ? null : values()[ordinal() + 1];
    }

    /** Direction vector. */
    protected final int dc, dr;

}

