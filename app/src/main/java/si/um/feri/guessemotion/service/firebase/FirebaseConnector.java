package si.um.feri.guessemotion.service.firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.UUID;
import java.sql.Timestamp;

import si.um.feri.guessemotion.ui.activity.MainMenuActivity;

public class FirebaseConnector {

    static private FirebaseUser user;
    static private DatabaseReference reference;
    static private UUID gameID;
    static private String question;

    public FirebaseConnector(){
        user = MainMenuActivity.getFirebaseUser();
        reference = MainMenuActivity.getReference();
    }

    static public void sendGame (UUID _gameID){
        gameID = _gameID;

        reference.child("users").child(user.getUid()).child("game").child(gameID.toString());
        reference.child("users").child(user.getUid()).child("game").child(gameID.toString()).child("date").setValue(new Timestamp(System.currentTimeMillis()));


    }

    static public void sendQuestion(String _question){
        question = _question;
        reference.child("users").child(user.getUid()).child("game").child(gameID.toString()).child(question).setValue(question);
    }

    static public void sendResult(int _result){
        String message;
        switch (_result) {
            // run out of the time
            case -1:
                message = "timeout";
                break;
            // correct answer
            case 0:
                message = "correct";
                break;
            // wrong answer
            case 1:
                message= "wrong";
                break;
            default:
                message = "null";
                break;
        }

        reference.child("users").child(user.getUid()).child("game").child(gameID.toString()).child(question).setValue(message);
    }

}
