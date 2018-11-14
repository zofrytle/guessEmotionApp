package si.um.feri.guessemotion.service.firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.UUID;
import java.sql.Timestamp;

import si.um.feri.guessemotion.game.data.enums.GamePlayMode;
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

    static public void sendGame (UUID _gameID, GamePlayMode gamePlayMode){
        gameID = _gameID;

        reference.child("users").child(user.getUid()).child(gamePlayMode.name()).child(gameID.toString()).setValue(gameID);
        reference.child("users").child(user.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child("date").setValue(new Timestamp(System.currentTimeMillis()));


    }

    static public void sendQuestion(String _question, GamePlayMode gamePlayMode){
        question = _question;
        reference.child("users").child(user.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child(question).setValue(question);
    }

    static public void sendAnswer(String _answer, GamePlayMode gamePlayMode){
        reference.child("users").child(user.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child(question).child("answer").setValue(_answer);

    }

    static public void sendCorrectAnswer(String _correctAnswer, GamePlayMode gamePlayMode){
        reference.child("users").child(user.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child(question).child("correct").setValue(_correctAnswer);

    }


    static public void sendResult(int _result, GamePlayMode gamePlayMode){
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

        reference.child("users").child(user.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child(question).child("result").setValue(message);
    }

}
