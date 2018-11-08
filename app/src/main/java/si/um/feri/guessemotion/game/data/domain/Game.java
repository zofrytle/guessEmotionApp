package si.um.feri.guessemotion.game.data.domain;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;
import java.util.UUID;

import si.um.feri.guessemotion.service.firebase.FirebaseConnector;

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
public class Game {
    private UUID uid;
    public Long startTimestamp;
    public Long endTimestamp;
    public String deviceUsedForPlaying;
    public Double locationLat;
    public Double locationLong;
    public String userUid;
    public List<String> questions;
    FirebaseUser firebaseUser;

    public Game() {
        // Default constructor required for calls to DataSnapshot.getValue(Game.class)
    }

    public Game(FirebaseUser user){
        this.uid = UUID.randomUUID();
        firebaseUser = user;
    }

    public Game(UUID uid, Long startTimestamp, Long endTimestamp, String deviceUsedForPlaying, Double locationLat, Double locationLong, String userUid, List<String> questions) {
        this.uid = uid;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.deviceUsedForPlaying = deviceUsedForPlaying;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.userUid = userUid;
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "uid:" + uid + " , " +
                "startTimestamp:" + startTimestamp + " , " +
                "endTimestamp:" + endTimestamp + " , " +
                "deviceUsedForPlaying:" + deviceUsedForPlaying + " , " +
                "locationLat:" + locationLat + " , " +
                "locationLong:" + locationLong + " , " +
                "userUid:" + userUid;
    }

    public UUID getUid() {
        return uid;
    }
}


//        "appInfo": {
//        "levelDescription" : "100,50,20,10",
//        "numberOfLives" : 30,
//        "numberOfQuestionsInLevel" : 7,
//        "timeAllowedToAnswer" : 40
//        },
//        "users": {
//        "idOfUser": {
//        "name" : "Ferko",
//        "games" : [1,2,3]
//        }
//        },
//        "games": {
//        "idOfGame": {
//        "startTimestamp" : 12345678,
//        "endTimestamp" : 87654321,
//        "deviceUsedForPlaying" : "whathere?",
//        "locationLat" : 10.20,
//        "locationLong" : 10.21,
//        "userId" : "idOfUser",
//        "questions" : [1,2,3]
//        }
//        },
//        "questions": {
//        "idOfQuestion": {
//        "recording": {
//        "filename" : "filename.mp3",
//        "level" : 1
//        },
//        "answer" : "happiness",
//        "gameId" : "idOfGame"
//        }
//        }
//        }