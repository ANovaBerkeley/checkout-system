package loa;

import static loa.Piece.*;

/** Represents a player.  Extensions of this class do the actual playing.
 *  @author P. N. Hilfinger
 */
public abstract class Player {

    /** A player that plays the SIDE pieces in GAME. */
    Player(Piece side, Game game) {
        _side = side;
        _game = game;
    }

    /** Return my next move from the current position in getBoard(), assuming
     *  that side() == getBoard.turn().  Returns null only if an intervening
     *  command stops play. */
    abstract Move makeMove();

    /** Return which side I'm playing. */
    Piece side() {
        return _side;
    }

    /** Return the board I am using. */
    Board getBoard() {
        return _game.getBoard();
    }

    /** Return the game I am playing. */
    Game getGame() {
        return _game;
    }

    /** This player's side. */
    private final Piece _side;
    /** The game this player is part of. */
    private Game _game;

}
