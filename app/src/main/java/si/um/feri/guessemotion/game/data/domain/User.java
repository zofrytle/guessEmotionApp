package si.um.feri.guessemotion.game.data.domain;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

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
public class User {

    public String uid;
    public String name;
    public String country;
    public List<String> games;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String name, String country) {
        this.uid = uid;
        this.name = name;
        this.country = country;
    }

    @Override
    public String toString() {
        return "uid:" + uid + " , " +
                "name:" + name + " , " +
                "country:" + country;
    }
}
