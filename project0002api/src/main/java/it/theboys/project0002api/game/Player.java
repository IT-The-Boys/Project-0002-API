package it.theboys.project0002api.game;

import java.util.LinkedList;
import java.util.List;

import it.theboys.project0002api.cards.WhiteCard;
import it.theboys.project0002api.data.User;


/**
 * Data required for a player in a {@code Game}.
 *
 * @author Andy Janata (ajanata@socialgamer.net)
 */
public class Player {
    public final List<WhiteCard> hand = new LinkedList<>();
    private final User user;
    private int score = 0;
    private int skipCount = 0;

    /**
     * Create a new player object.
     *
     * @param user The {@code User} associated with this player.
     */
    public Player(User user) {
        this.user = user;
    }

    /**
     * @return The {@code User} associated with this player.
     */
    public User getUser() {
        return user;
    }

    /**
     * @return The player's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Increase the player's score by 1 point.
     */
    public void increaseScore() {
        score++;
    }

    /**
     * Reset the player's score to 0.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Increases this player's skipped round count.
     */
    public void skipped() {
        skipCount++;
    }

    /**
     * Reset this player's skipped round count to 0, because they have been back for a round.
     */
    public void resetSkipCount() {
        skipCount = 0;
    }

    /**
     * @return This player's skipped round count.
     */
    public int getSkipCount() {
        return skipCount;
    }

    @Override
    public String toString() {
        return String.format("%s (%dp, %ds)", user.toString(), score, skipCount);
    }
}