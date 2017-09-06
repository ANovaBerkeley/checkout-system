package loa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static loa.Piece.*;
import static loa.Main.*;

/**
 * Represents one game of Lines of Action.
 * @author Yuki Mizuno
 */
class Game {

    /** A new series of Games. */
    Game() {
        _randomSource = new Random();

        _players = new Player[2];
        _input = new BufferedReader(new InputStreamReader(System.in));
        _players[0] = new HumanPlayer(BP, this);
        _players[1] = new MachinePlayer(WP, this);
        _playing = false;
    }

    /** Return the current board. */
    Board getBoard() {
        return _board;
    }

    /** Quit the game. */
    private void quit() {
        System.exit(0);
    }

    /**
     * Return a move. Processes any other intervening commands as well. Exits
     * with null if the value of _playing changes.
     */
    Move getMove() {
        try {
            boolean playing0 = _playing;
            while (_playing == playing0) {
                prompt();

                String line = _input.readLine();
                if (line == null) {
                    quit();
                }

                line = line.trim();
                if (!processCommand(line)) {
                    Move move = Move.create(line, _board);
                    if (move == null) {
                        error("invalid move: %s%n", line);
                    } else if (!_playing) {
                        error("game not started");
                    } else if (!_board.isLegal(move)) {
                        error("illegal move: %s%n", line);
                    } else {
                        return move;
                    }
                }
            }
        } catch (IOException excp) {
            error(1, "unexpected I/O error on input");
        }
        return null;
    }

    /** Print a prompt for a move. */
    private void prompt() {
        System.out.print("> ");
        System.out.flush();
    }

    /** Describes a command with up to two arguments. */
    private static final Pattern COMMAND_PATN =
            Pattern.compile("(#|\\S+)\\s*(\\S*)\\s*(\\S*).*");

    /**
     * If LINE is a recognized command other than a move, process it and return
     * true. Otherwise, return false.
     */
    private boolean processCommand(String line) {
        if (line.length() == 0) {
            return true;
        }
        Matcher command = COMMAND_PATN.matcher(line);
        if (command.matches()) {
            switch (command.group(1).toLowerCase()) {
            case "#":
                return true;
            case "manual":
                manualCommand(command.group(2).toLowerCase());
                return true;
            case "auto":
                autoCommand(command.group(2).toLowerCase());
                return true;
            case "seed":
                seedCommand(command.group(2));
                return true;
            case "dump":
                System.out.println(_board);
                return true;
            case "clear":
                _board.clear();
                return true;
            case "autoprint":
                return true;
            case "set":
                setCommand(command.group(2).toLowerCase(),
                    command.group(3).toLowerCase());
                return true;
            case "start":
                _playing = true;
                return true;
            case "quit":
            case "q":
                quit();
                return true;
            case "help":
            case "?":
                help();
                return true;
            default:
                return false;
            }
        }
        return false;
    }

    /** Command to set the pieces using PIECES and PLAYER. */
    private void setCommand(String pieces, String player) {
        try {
            int c;
            int r;
            Piece p;
            c = Board.col(pieces);
            r = Board.row(pieces);
            p = Piece.setValueOf(player);
            _playing = false;
            _board.set(c, r, p, p.opposite());
        } catch (IllegalArgumentException ex) {
            error("unknown player: %p", player);
        }
    }

    /** Set player PLAYER ("white" or "black") to be a manual player. */
    private void manualCommand(String player) {
        try {
            Piece s = Piece.playerValueOf(player);
            _playing = false;
            _players[s.ordinal()] = new HumanPlayer(s, this);
        } catch (IllegalArgumentException excp) {
            error("unknown player: %s", player);
        }
    }

    /** Set player PLAYER ("white" or "black") to be an automated player. */
    private void autoCommand(String player) {
        try {
            Piece s = Piece.playerValueOf(player);
            _playing = false;
            _players[s.ordinal()] = new MachinePlayer(s, this);
        } catch (IllegalArgumentException excp) {
            error("unknown player: %s", player);
        }
    }

    /** Seed random-number generator with SEED (as a long). */
    private void seedCommand(String seed) {
        try {
            _randomSource.setSeed(Long.parseLong(seed));
        } catch (NumberFormatException excp) {
            error("Invalid number: %s", seed);
        }
    }

    /** Play this game, printing any results. */
    public void play() {
        HashSet<Board> positionsPlayed = new HashSet<Board>();
        _board = new Board();

        while (true) {
            int playerInd = _board.turn().ordinal();
            Move next;
            if (_playing) {
                if (_board.gameOver()) {
                    announceWinner();
                    _playing = false;
                    continue;
                }
                next = _players[playerInd].makeMove();
                assert !_playing || next != null;
            } else {
                getMove();
                next = null;
            }
            if (next != null) {
                assert _board.isLegal(next);
                _board.makeMove(next);
                if (_board.gameOver()) {
                    announceWinner();
                    _playing = false;
                }
            }
        }
    }

    /** Print an announcement of the winner. */
    private void announceWinner() {
        if (_board.piecesContiguous(BP)
                && _board.piecesContiguous(WP)) {
            if (_board.turn() == BP) {
                System.out.println("Black wins.");
            } else if (_board.turn() == WP) {
                System.out.println("White wins.");
            }
        } else {
            if (_board.piecesContiguous(BP)) {
                System.out.println("Black wins.");
            } else {
                System.out.println("White wins.");
            }
        }
    }

    /**
     * Return an integer r, 0 <= r < N, randomly chosen from a uniform
     * distribution using the current random source.
     */
    int randInt(int n) {
        return _randomSource.nextInt(n);
    }

    /** Print a help message. */
    void help() {
        Main.usage();
    }

    /** The official game board. */
    private Board _board;

    /** The _players of this game. */
    private Player[] _players = new Player[2];

    /**
     * A source of random numbers, primed to deliver the same sequence in any
     * Game with the same seed value.
     */
    private Random _randomSource;

    /** Input source. */
    private BufferedReader _input;

    /**
     * True if actually playing (game started and not stopped or finished).
     */
    private boolean _playing;

}
