package loa;

import ucb.junit.textui;
import org.junit.Test;

import static loa.Piece.BP;
import static loa.Piece.EMP;
import static loa.Piece.WP;
import static org.junit.Assert.*;

import java.util.Iterator;

/** The suite of all JUnit tests for the loa package.
 *  @author Yuki Mizuno
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }
    @Test
    public void testBoard() {
        Board boar = new Board();
        Piece[][] pieces = { { EMP, BP, BP, BP, BP, BP, BP, EMP },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP } };
        Board end = new Board(pieces, Piece.BP);
        System.out.println(end);
        assertEquals(true, end.piecesContiguous(Piece.BP));
        System.out.println(boar);
        assertEquals(false, boar.piecesContiguous(Piece.BP));
        Piece[][] pieces2 = { { EMP, BP, BP, BP, BP, BP, BP, EMP },
            { WP, EMP, EMP, BP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, BP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, BP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, BP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, BP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, BP, EMP, EMP, EMP, WP },
            { EMP, EMP, EMP, BP, EMP, EMP, EMP, EMP } };
        Board end2 = new Board(pieces2, Piece.BP);
        System.out.println(end2);
        assertEquals(true, end2.piecesContiguous(Piece.BP));
    }
    @Test
    public void testIsLegalAndMisc() {
        Board boar = new Board();
        Move mov1 = Move.create("b1-b3", boar);
        Move mov3 = Move.create("a3-c3", boar);
        Direction dir1 = boar.whichDir(mov1);
        assertEquals(Direction.N, dir1);
        assertEquals(2, boar.pieceCountAlong(mov1));
        assertEquals(false, boar.blocked(mov1));
        assertEquals(true, boar.isLegal(mov1));
        boar.makeMove(mov1);
        System.out.println(boar);
        assertEquals(false, boar.isLegal(mov3));
        boar.makeMove(mov3);
        System.out.println(boar);
        Move mov4 = Move.create("b3-b5", boar);
        assertEquals(false, boar.isLegal(mov4));
        assertEquals(true, boar.blocked(mov4));
        boar.makeMove(mov4);
    }
    @Test
    public void testIncr() {
        Board board = new Board();
        int count = 0;
        Iterator<Move> moves = board.iterator();
        while (moves.hasNext()) {
            count++;
            Move move = moves.next();
            assertTrue(board.isLegal(move));
        }
        assertEquals(count, 36);
    }
}


