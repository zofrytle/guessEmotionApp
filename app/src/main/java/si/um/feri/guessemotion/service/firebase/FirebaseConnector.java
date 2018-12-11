package si.um.feri.guessemotion.service.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.UUID;

import si.um.feri.guessemotion.game.data.enums.GamePlayMode;
import si.um.feri.guessemotion.ui.activity.MainMenuActivity;

public class FirebaseConnector {

    static private FirebaseUser firebaseUser;
    static private DatabaseReference reference;
    static private UUID gameID;
    static private String question;

    public FirebaseConnector(){
        reference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = MainMenuActivity.getFirebaseUser();
    }

    static public void sendGame (UUID _gameID, GamePlayMode gamePlayMode){
        gameID = _gameID;

        reference.child("users").child(firebaseUser.getUid()).child(gamePlayMode.name()).child(gameID.toString()).setValue(gameID);
        reference.child("users").child(firebaseUser.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child("date").setValue(new Timestamp(System.currentTimeMillis()));
        reference.child("users").child(firebaseUser.getUid()).child("RANKED").child(gameID.toString()).child("score").setValue(0);

    }

    static public void sendQuestion(String _question, GamePlayMode gamePlayMode){
        question = _question;
        reference.child("users").child(firebaseUser.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child(question).setValue(question);
    }

    static public void sendAnswer(String _answer, String _correctAnswer, GamePlayMode gamePlayMode){
        reference.child("users").child(firebaseUser.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child(question).child("answer").setValue(_answer);
        reference.child("users").child(firebaseUser.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child(question).child("correct").setValue(_correctAnswer);
    }

    static public void sendResult(int _result, GamePlayMode gamePlayMode){
        final String message;
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

        reference.child("users").child(firebaseUser.getUid()).child(gamePlayMode.name()).child(gameID.toString()).child(question).child("result").setValue(message);

        if(gamePlayMode.name().equals("RANKED")) {


            reference.child("users").child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot childSnapshot = dataSnapshot.child("RANKED").child(gameID.toString()).child("score");
                    DataSnapshot totalScoreSnapshot = dataSnapshot.child("RANKED").child("totalScore");

                    Long total;
                    if(totalScoreSnapshot.getValue() == null) {
                        reference.child("users").child(firebaseUser.getUid()).child("RANKED").child("totalScore").setValue(0);
                        total = Long.valueOf(0);
                    }
                    else
                        total = (Long) dataSnapshot.child("RANKED").child("totalScore").getValue();


                    @NonNull
                    Long value = (Long) childSnapshot.getValue();

                    if (message.equals("correct")) {
                        value = value + 20;
                        total = total + 20;
                        reference.child("users").child(firebaseUser.getUid()).child("RANKED").child(gameID.toString()).child("score").setValue(value);
                        reference.child("users").child(firebaseUser.getUid()).child("RANKED").child("totalScore").setValue(total);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}