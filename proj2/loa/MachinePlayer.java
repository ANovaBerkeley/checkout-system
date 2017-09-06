package loa;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An automated Player.
 * @author Yuki Mizuno
 */
class MachinePlayer extends Player {
    /** Returns current player. */
    private Piece _player;

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
        _player = side;
    }
    /** Gets the player returns _PLAYER. */
    Piece getPlayer() {
        return _player;
    }

    @Override
    Move makeMove() {
        int highScore = Integer.MIN_VALUE;
        int curScore = Integer.MIN_VALUE;
        Board game = getBoard();
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int depth = 1;
        Move bestMove = null;
        Board tryBoard = new Board(game);
        Iterator<Move> moveiter = tryBoard.iterator();

        while (moveiter.hasNext()) {
            Move curMove = moveiter.next();
            tryBoard.makeMove(curMove);
            curScore = alphaBetaPruning(tryBoard.turn().opposite(),
                    tryBoard, alpha, beta, depth - 1);
            if (curScore > highScore) {
                highScore = curScore;
                bestMove = curMove;
            }
            tryBoard.retract();
        }
        if (side().abbrev().equals("b")) {
            System.out.println("B::" + bestMove);
        }
        if (side().abbrev().equals("w")) {
            System.out.println("W::" + bestMove);
        }
        return bestMove;
    }
    /** Helper to retrieve move, takes in PLAYER, DEPTH, ALPHA,
     * BETA, GAME, and returns Move. */
    Move helperMove(int depth, int alpha, int beta, Board game) {
        return null;
    }
    /**
     * Returns an integer evaluation of the board. Higher the better. This is
     * where the actual evaluation of the move is done using BOARD. */
    public int eval(Board board) {
        if (board.piecesContiguous(side())) {
            return Integer.MAX_VALUE;
        }
        if (board.piecesContiguous(side().opposite())) {
            return Integer.MIN_VALUE;
        }
        ArrayList<int[]> manual = new ArrayList<int[]>();
        ArrayList<int[]> artif = new ArrayList<int[]>();
        Board bobo = new Board(board);
        for (int c = 1; c <= 8; c += 1) {
            for (int r = 1; r <= 8; r += 1) {
                if (bobo.get(c, r) == side()) {
                    int[] a = { c, r };
                    artif.add(a);
                } else if (bobo.get(c, r) == side().opposite()) {
                    int[] a = { c, r };
                    manual.add(a);
                }
            }
        }
        int distA = findDist(artif); int distM = findDist(manual);
        return distM - distA;
    }
    /** Helper method to find distance using HC which returns TOTAL. */
    int findDist(ArrayList<int[]> hc) {
        int total = 0;
        double dist; double x; double y;
        for (int i = 0; i < hc.size(); i += 1) {
            for (int j = 0; j < hc.size(); j += 1) {
                x = Math.pow(hc.get(i)[0] - hc.get(j)[0], 2);
                y = Math.pow(hc.get(i)[1] - hc.get(j)[1], 2);
                dist = Math.sqrt(x + y);
                total += Math.round(dist);
            }
        }
        return total;
    }

    /** Method for alpha beta pruning. Only for pruning purposes
     * using PLAYER, BOARD, A, B, DEPTH which returns an INT. */
    int alphaBetaPruning(Piece player, Board board, int a, int b, int depth) {
        if (board.gameOver() || depth <= 0) {
            return eval(board);
        }
        int curScore;
        Board tryBoard = new Board(board);
        Iterator<Move> abiter = tryBoard.iterator();
        ArrayList<Board> boardStates = new ArrayList<Board>();
        while (abiter.hasNext()) {
            Move nextMove = abiter.next();
            tryBoard.makeMove(nextMove);
            boardStates.add(tryBoard);
            tryBoard.retract();
        }
        if (board.turn() == side()) {
            for (Board boards : boardStates) {
                curScore = alphaBetaPruning(player.opposite(),
                        boards, a, b, depth - 1);
                if (curScore > a) {
                    a = curScore;
                }
                if (a >= b) {
                    return a;
                }
            }
            return a;
        } else {
            for (Board boards : boardStates) {
                curScore = alphaBetaPruning(player.opposite(),
                        boards, a, b, depth - 1);
                if (curScore < b) {
                    b = curScore;
                }
                if (a >= b) {
                    return b;
                }
            }
            return b;
        }
    }
}
