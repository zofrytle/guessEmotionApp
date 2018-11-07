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
public class Question {

    public String uid;
    public String filename;
    public Long level;
    public String answer;
    public String gameUid;

    public Question() {
        // Default constructor required for calls to DataSnapshot.getValue(Question.class)
    }

    public Question(String uid, String filename, Long level, String answer, String gameUid) {
        this.uid = uid;
        this.filename = filename;
        this.level = level;
        this.answer = answer;
        this.gameUid = gameUid;
    }

    @Override
    public String toString() {
        return "uid:" + uid + " , " +
                "filename:" + filename + " , " +
                "level:" + level + " , " +
                "answer:" + answer + " , " +
                "gameUid:" + gameUid;
    }
}
