package loa;

/** A Piece denotes the contents of a square, or identifies one side
 *  (Black or White) of a game.
 *  @author Yuki Mizuno
 */
enum Piece {
    /** The names of the pieces.  EMP indicates an empty square. The
     *  arguments give names to the piece colors. */
    BP, WP, EMP;

    /** Returns the full name of this piece (black, white, or
     *  empty). */
    String fullName() {
        switch (this) {
        case BP:
            return "black";
        case WP:
            return "white";
        default:
            return "empty";
        }
    }

    /** Returns the one-character denotation of this piece on the standard
     *  text display of a board. */
    String abbrev() {
        switch (this) {
        case BP:
            return "b";
        case WP:
            return "w";
        default:
            return "-";
        }
    }

    /** Return player (white or black piece) for which  .fullName()
     *  returns NAME. */
    static Piece playerValueOf(String name) {
        switch (name.toLowerCase()) {
        case "black":
            return BP;
        case "white":
            return WP;
        default:
            throw new IllegalArgumentException("piece name unknown");
        }
    }

    /** Return the Piece denoted by NAME in set commands. */
    static Piece setValueOf(String name) {
        switch (name) {
        case "b":
            return BP;
        case "w":
            return WP;
        case "e":
            return EMP;
        default:
            throw new IllegalArgumentException("piece name unknown");
        }
    }

    /** Returns Piece with my opposing color (null for EMP). */
    Piece opposite() {
        switch (this) {
        case BP:
            return WP;
        case WP:
            return BP;
        default:
            return null;
        }
    }

    /** The textual representation of this piece. */
    private String _fullName;
    /**
     * The one-character abbreviation of this piece, used in printed
     * representations ot the board.
     */
    private String _abbrev;
}
