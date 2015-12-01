package loa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Formatter;
import java.util.NoSuchElementException;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Direction.*;

/**
 * Represents the state of a game of Lines of Action.
 * @author Yuki Mizuno
 */
class Board implements Iterable<Move> {

    /** Size of a board. */
    static final int M = 8;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /**
     * A Board whose initial contents are taken from INITIALCONTENTS and in
     * which the player playing TURN is to move. The resulting Board has
     * get(col, row) == INITIALCONTENTS[row-1][col-1] Assumes that PLAYER is not
     * null and INITIALCONTENTS is MxM.
     *
     * CAUTION: The natural written notation for arrays initializers puts the
     * BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        clear();
    }

    /**
     * A Board whose initial contents and state are copied from BOARD.
     */
    Board(Board board) {
        _curContents = new Piece[8][8];
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        _moves.clear();
        _curContents = new Piece[8][8];
        _curContents = contents;
        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                set(c, r, contents[r - 1][c - 1]);
            }
        }
        _turn = side;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        _moves.clear();
        _moves.addAll(board._moves);
        _turn = board._turn;
        for (int col = 1; col <= M; col = col + 1) {
            for (int row = 1; row <= M; row = row + 1) {
                set(col, row, board.get(col, row));
            }
        }
        this._curContents = board._curContents;
    }

    /**
     * Return the contents of column C, row R, where 1 <= C,R <= 8, where column
     * 1 corresponds to column 'a' in the standard notation.
     */
    Piece get(int c, int r) {
        assert (1 <= c && 1 <= r && c <= 8 && r <= 8);
        return _curContents[r - 1][c - 1];
    }

    /**
     * Return the contents of the square SQ. SQ must be the standard printed
     * designation of a square (having the form cr, where c is a letter from a-h
     * and r is a digit from 1-8).
     */
    Piece get(String sq) {
        return get(col(sq), row(sq));
    }

    /**
     * Return the column number (a value in the range 1-8) for SQ. SQ is as for
     * {@link get(String)}.
     */
    static int col(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(0) - 'a' + 1;
    }

    /**
     * Return the row number (a value in the range 1-8) for SQ. SQ is as for
     * {@link get(String)}.
     */
    static int row(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(1) - '0';
    }

    /**
     * Set the square at column C, row R to V, and make NEXT the next side to
     * move, if it is not null.
     */
    void set(int c, int r, Piece v, Piece next) {
        _curContents[r - 1][c - 1] = v;
        if (next != null) {
            _turn = next;
        }
    }

    /** Set the square at column C, row R to V. */
    void set(int c, int r, Piece v) {
        set(c, r, v, null);
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    void makeMove(Move move) {
        if (!(isLegal(move))) {
            return;
        }
        _moves.add(move);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        if (replaced != EMP) {
            set(c1, r1, EMP);
        }
        set(c1, r1, move.movedPiece());
        set(c0, r0, EMP);
        _turn = _turn.opposite();
    }

    /**
     * (unmake) one move, returning to the state immediately before that move.
     * Requires that movesMade () > 0.
     */
    void retract() {
        assert movesMade() > 0;
        Move move = _moves.remove(_moves.size() - 1);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        Piece movedPiece = move.movedPiece();
        set(c1, r1, replaced);
        set(c0, r0, movedPiece);
        _turn = _turn.opposite();
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff MOVE is legal for the player currently on move. */
    boolean isLegal(Move move) {
        if ((move == null) || blocked(move)
                || (move.length() != pieceCountAlong(move))
                || !(move.movedPiece() == _turn)) {
            return false;
        }
        return true;
    }

    /** Return a sequence of all legal moves from this position. */
    Iterator<Move> legalMoves() {
        return new MoveIterator();
    }

    @Override
    public Iterator<Move> iterator() {
        return legalMoves();
    }

    /**
     * Return true if there is at least one legal move for the player on move.
     */
    public boolean isLegalMove() {
        return iterator().hasNext();
    }

    /** Return true iff either player has all his pieces contiguous. */
    boolean gameOver() {
        return piecesContiguous(BP) || piecesContiguous(WP);
    }

    /** Return true iff SIDE's pieces are contiguous. */
    boolean piecesContiguous(Piece side) {
        boolean[][] marker = new boolean[8][8];
        int col = 0;
        int row = 0;
        int totalPieces = 0;
        for (int c = 0; c < 8; c = c + 1) {
            for (int r = 0; r < 8; r = r + 1) {
                if ((side == BP && _curContents[c][r] == BP)
                        || (side == WP && _curContents[c][r] == WP)) {
                    totalPieces = totalPieces + 1;
                    col = c;
                    row = r;
                }
            }
        }
        int boolTotal = helperContiguous(side, marker, col, row);
        return totalPieces == boolTotal;
    }

    /**
     * Helper method for the piecesContiguous method from SIDE, BOOTY, COL &
     * ROW returns INT.
     */
    int helperContiguous(Piece side, boolean[][] booty, int col, int row) {
        booty[col][row] = true;
        int boolTotal = 1;
        for (int cdir = -1; cdir < 2; cdir = cdir + 1) {
            for (int rdir = -1; rdir < 2; rdir = rdir + 1) {
                if (cdir == 0 && rdir == 0) {
                    continue;
                }
                int newcol = col + cdir;
                int newrow = row + rdir;
                if (newcol <= -1 || newrow <= -1
                    || newcol >= 8 || newrow >= 8) {
                    continue;
                }
                if ((side == BP && _curContents[newcol][newrow] != BP)
                        || (side == WP && _curContents[newcol][newrow] != WP)) {
                    continue;
                }
                if (booty[newcol][newrow]) {
                    continue;
                }
                boolTotal = boolTotal
                    + helperContiguous(side, booty, newcol, newrow);
            }
        }
        return boolTotal;
    }

    /**
     * Return the total number of moves that have been made (and not retracted).
     * Each valid call to makeMove with a normal move increases this number by
     * 1.
     */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        if (turn() != b.turn()) {
            return false;
        }
        for (int c = 1; c <= M; c++) {
            for (int r = 1; r <= M; r++) {
                if (get(c, r) != b.get(c, r)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (_curContents[c][r] == EMP) {
                    hash = M * hash + 0;
                } else if (_curContents[c][r] == WP) {
                    hash = M * hash + 1;
                } else if (_curContents[c][r] == BP) {
                    hash = M * hash + 2;
                }
            }
        }
        if (_turn == WP) {
            hash = -hash;
        }
        return hash;
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = M; r >= 1; r -= 1) {
            out.format("    ");
            for (int c = 1; c <= M; c += 1) {
                out.format("%s ", get(c, r).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return the number of pieces in the line of action indicated by MOVE. */
    public int pieceCountAlong(Move move) {
        int curCol = move.getCol0();
        int curRow = move.getRow0();
        Direction dir = whichDir(move);
        return pieceCountAlong(curCol, curRow, dir);
    }

    /**
     * Return the number of pieces in the line of action in direction DIR and
     * containing the square at column C and row R.
     */
    public int pieceCountAlong(int c, int r, Direction dir) {
        int countPieces = 0;
        if (dir == N || dir == S) {
            for (int row = 1; row <= 8; row = row + 1) {
                if (get(c, row) != EMP) {
                    countPieces = countPieces + 1;
                }
            }
        } else if (dir == E || dir == W) {
            for (int col = 1; col <= 8; col = col + 1) {
                if (get(col, r) != EMP) {
                    countPieces = countPieces + 1;
                }
            }
        } else if (dir == NW || dir == SE) {
            if (get(c, r) != EMP) {
                countPieces = countPieces + 1;
            }
            int cvar = c + NW.dc;
            int rvar = r + NW.dr;
            while (cvar <= 8 && rvar <= 8 && cvar > 0 && rvar > 0) {
                if (get(cvar, rvar) != EMP) {
                    countPieces = countPieces + 1;
                }
                rvar = rvar + NW.dr;
                cvar = cvar + NW.dc;
            }
            cvar = c + SE.dc;
            rvar = r + SE.dr;
            while (cvar <= 8 && rvar <= 8 && cvar > 0 && rvar > 0) {
                if (get(cvar, rvar) != EMP) {
                    countPieces = countPieces + 1;
                }
                rvar = rvar + SE.dr;
                cvar = cvar + SE.dc;
            }
        } else if (dir == NE || dir == SW) {
            if (get(c, r) != EMP) {
                countPieces = countPieces + 1;
            }
            int cvar = c + NE.dc;
            int rvar = r + NE.dr;
            while (cvar <= 8 && rvar <= 8 && cvar > 0 && rvar > 0) {
                if (get(cvar, rvar) != EMP) {
                    countPieces = countPieces + 1;
                }
                rvar = rvar + NE.dr;
                cvar = cvar + NE.dc;
            }
            cvar = c + SW.dc;
            rvar = r + SW.dr;
            while (cvar <= 8 && rvar <= 8 && cvar > 0 && rvar > 0) {
                if (get(cvar, rvar) != EMP) {
                    countPieces = countPieces + 1;
                }
                rvar = rvar + SW.dr; cvar = cvar + SW.dc;
            }
        }
        return countPieces;
    }

    /** A helper function that returns the direction of the MOVE. */
    public Direction whichDir(Move move) {
        int curCol = move.getCol0();
        int curRow = move.getRow0();
        int endCol = move.getCol1();
        int endRow = move.getRow1();
        int diffCol = endCol - curCol;
        int diffRow = endRow - curRow;
        if (diffCol > 0 && diffRow == 0) {
            return E;
        } else if (diffCol < 0 && diffRow == 0) {
            return W;
        } else if (diffRow > 0 && diffCol == 0) {
            return N;
        } else if (diffRow < 0 && diffCol == 0) {
            return S;
        } else if (diffRow > 0 && diffCol > 0) {
            return NE;
        } else if (diffRow > 0 && diffCol < 0) {
            return NW;
        } else if (diffRow < 0 && diffCol > 0) {
            return SE;
        } else if (diffRow < 0 && diffCol < 0) {
            return SW;
        }
        return NOWHERE;
    }

    /** Return true iff MOVE is blocked by an opposing piece or by a friendly
    * piece on the target square. */
    public boolean blocked(Move move) {
        int curCol = move.getCol0();
        int curRow = move.getRow0();
        int endCol = move.getCol1();
        int endRow = move.getRow1();
        if (get(endCol, endRow) == _turn) {
            return true;
        }
        Direction dir = whichDir(move);
        while (curCol != endCol || curRow != endRow) {
            if (get(curCol, curRow) == _turn.opposite()) {
                return true;
            }
            curCol = curCol + dir.dc;
            curRow = curRow + dir.dr;
        }
        return false;
    }
    /** The standard initial configuration for Lines of Action. */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP, BP, BP, BP, BP, BP, EMP },
        { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
        { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
        { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
        { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
        { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
        { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
        { EMP, BP, BP, BP, BP, BP, BP, EMP } };

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** The current contents of the board. */
    private Piece[][] _curContents;

    /** An iterator returning the legal moves from the current board. */
    private class MoveIterator implements Iterator<Move> {
        /** New list to store legal moves. */
        private ArrayList<Move> _listMoves;
        /** Iterator for _listMoves. */
        private Iterator<Move> _movesIter;
        /** Current piece under consideration. */
        private int _c, _r;
        /** Next direction of current piece to return. */
        private Direction _dik;
        /** Next move. */
        private Move _move;
        /** The board. */
        private Board _curBoard;

        /** A new move iterator for turn(). */
        MoveIterator() {
            _c = 1;
            _r = 1;
            _dik = N;
            _listMoves = new ArrayList<Move>();
            _curBoard = new Board(_curContents, _turn);
            incr();
            _movesIter = _listMoves.iterator();
            moveSetter();
        }

        @Override
        public boolean hasNext() {
            return _move != null;
        }

        @Override
        public Move next() {
            if (_move == null) {
                throw new NoSuchElementException("no legal move");
            }

            Move move = _move;
            moveSetter();
            return move;
        }

        @Override
        public void remove() {
        }
        /** Helper for the iterator. */
        private void moveSetter() {
            if (_movesIter.hasNext()) {
                Move next = _movesIter.next();
                _move = next;
            } else {
                _move = null;
            }
        }
        /** Advance to the next legal move. */
        private void incr() {
            for (int i = _c; i <= M; i = i + 1) {
                for (int j = _r; j <= M; j = j + 1) {
                    _dik = N;
                    Piece piec = get(i, j);
                    if (piec == _turn) {
                        while (_dik != null) {
                            Move mov = Move.create(i, j,
                                    pieceCountAlong(i, j, _dik), _dik,
                                    _curBoard);
                            if (isLegal(mov)) {
                                _listMoves.add(mov);
                            }
                            _dik = _dik.succ();
                        }
                    }
                }
            }
        }
    }
}
