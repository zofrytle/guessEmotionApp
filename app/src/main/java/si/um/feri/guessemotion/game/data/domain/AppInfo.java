package si.um.feri.guessemotion.game.data.domain;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

@IgnoreExtraProperties
public class AppInfo {

    public String levelDescription;
    public Long numberOfLives;
    public Long numberOfQuestionsInLevel;
    public Long timeAllowedToAnswer;

    public AppInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(AppInfo.class)
    }

    public AppInfo(String levelDescription, Long numberOfLives, Long numberOfQuestionsInLevel, Long timeAllowedToAnswer) {
        this.levelDescription = levelDescription;
        this.numberOfLives = numberOfLives;
        this.numberOfQuestionsInLevel = numberOfQuestionsInLevel;
        this.timeAllowedToAnswer = timeAllowedToAnswer;
    }

    @Override
    public String toString() {
        return "levelDescription:" + levelDescription + " , " +
                "numberOfLives:" + numberOfLives + " , " +
                "numberOfQuestionsInLevel:" + numberOfQuestionsInLevel + " , " +
                "timeAllowedToAnswer:" + timeAllowedToAnswer;
    }
}
