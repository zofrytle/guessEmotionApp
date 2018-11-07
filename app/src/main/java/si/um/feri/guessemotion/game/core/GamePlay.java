package si.um.feri.guessemotion.game.core;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import si.um.feri.guessemotion.App;
import si.um.feri.guessemotion.R;
import si.um.feri.guessemotion.game.data.domain.Game;
import si.um.feri.guessemotion.game.data.domain.Question;
import si.um.feri.guessemotion.game.data.domain.User;
import si.um.feri.guessemotion.game.data.enums.GamePlayMode;
import si.um.feri.guessemotion.game.data.enums.GamePlayState;
import si.um.feri.guessemotion.game.data.enums.Mood;
import si.um.feri.guessemotion.service.firebase.FirebaseConnector;
import si.um.feri.guessemotion.util.AndroidUtils;
import si.um.feri.guessemotion.util.Constants;
import si.um.feri.guessemotion.util.StorageUtils;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class GamePlay {

    private static final String TAG = "GamePlay";

    // basic game play fields
    private Context mContext;
    private int points;
    private int lives;
    private int timeAllowedToAnswer;
    private int numberOfQuestionsInLevel;
    private int numberOfLevels;
    private GamePlayMode gamePlayMode;
    private GamePlayState gamePlayState;
    private String[] recordsTitles;
    private int numberOfRecords;

    // previous values in useful fields
    private int prvsAnswerOrder;
    private Mood prvsCorrectAnswer;
    private int prvsEvaluate;

    // actual positions during game play
    private int actlPositionLevel;
    private int actlPositionQuestion;
    private String actlRecordTitle;
    private Mood actlCorrectAnswer;
    // TODO: 8.1.18 add level description..

    // for database purpose
    private Game dbGame;
    private User dbUser;
    private List<Question> dbQuestions;

    public GamePlay(Context context, GamePlayMode gamePlayMode) {
        this.lives = Constants.DEFAULT_NUMBER_OF_LIVES;
        this.timeAllowedToAnswer = Constants.DEFAULT_TIME_ALLOWED_TO_ANSWER;
        this.numberOfQuestionsInLevel = Constants.DEFAULT_NUMBER_OF_QUESTIONS_IN_LEVEL;
        this.numberOfLevels = Constants.DEFAULT_NUMBER_OF_LEVELS;
        this.mContext = context;
        this.gamePlayMode = gamePlayMode;
        initializeSongTitles();
        initGamePlay();
    }

    public GamePlay(Context context, GamePlayMode gamePlayMode, Integer lives, Integer timeAllowedToAnswer,
                    Integer numberOfQuestionsInLevel, Integer numberOfLevels) {
        this.lives = lives != null ? lives : Constants.DEFAULT_NUMBER_OF_LIVES;
        this.timeAllowedToAnswer = timeAllowedToAnswer != null ? timeAllowedToAnswer : Constants.DEFAULT_TIME_ALLOWED_TO_ANSWER;
        this.numberOfQuestionsInLevel = numberOfQuestionsInLevel != null ? numberOfQuestionsInLevel : Constants.DEFAULT_NUMBER_OF_QUESTIONS_IN_LEVEL;
        this.numberOfLevels = numberOfLevels != null ? numberOfLevels : Constants.DEFAULT_NUMBER_OF_LEVELS;
        this.mContext = context;
        this.gamePlayMode = gamePlayMode;
        initializeSongTitles();
        initGamePlay();
    }

    private void initializeSongTitles() {
        if (mContext == null) mContext = App.getContext();
        try {
            recordsTitles = mContext.getAssets().list("");
            numberOfRecords = recordsTitles.length;
        } catch (IOException e) {
            // TODO: 8.1.18 handle this somehow?
            e.printStackTrace();
        }
    }

    private void initGamePlay() {
        points = 0;
        actlPositionLevel = 1;
        actlPositionQuestion = 1;
        actlRecordTitle = getRandomRecordTitle();
        setActlCorrectAnswer();
        gamePlayState = GamePlayState.WAITING_FOR_PLAYING_RECORD;
    }

    /**
     * @param answerOrder
     * @return
     */
    public int evaluateQuestion(int answerOrder) {
        gamePlayState = GamePlayState.WAITING_FOR_NEXT_LEVEL;
        if (answerOrder == -1) {
            // run out of time
            handleWrongAnswer(answerOrder);
            return -1;
        } else {
            Mood moodAnswer = getMoodFromAnswerOrder(answerOrder);
            if (moodAnswer == actlCorrectAnswer) {
                handleCorrectAnswer(answerOrder);
                return 0;
            } else {
                handleWrongAnswer(answerOrder);
                return 1;
            }
        }
    }

    private void handleCorrectAnswer(int answerOrder) {
        prvsAnswerOrder = answerOrder;
        prvsCorrectAnswer = actlCorrectAnswer;
        prvsEvaluate = 0;
        increasePoints(20);
    }

    private void handleWrongAnswer(int answerOrder) {
        prvsAnswerOrder = answerOrder;
        prvsCorrectAnswer = actlCorrectAnswer;
        prvsEvaluate = answerOrder == -1 ? answerOrder : 1;
        if (gamePlayMode == GamePlayMode.RANKED) decreaseLives(1);
    }


    private Mood getMoodFromAnswerOrder(int answerOrder) {
        switch (answerOrder) {
            case 0:
                return Mood.JOY;
            case 1:
                return Mood.ANGER;
            case 2:
                return Mood.NEUTRAL_FAST;
            case 3:
                return Mood.NEUTRAL_SLOW;
            case 4:
                return Mood.SADNESS;
            case 5:
                return Mood.SURPRISE;
            case 6:
                return Mood.DISGUST;
            case 7:
                return Mood.FEAR;
        }
        return null;
    }

    private void increasePoints(int value) {
        this.points += value;
    }

    private void decreaseLives(int value) {
        this.lives -= value;
    }

    private boolean gameOver() {
        return lives <= 0;
    }

    private void setActlCorrectAnswer() {
        char moodChar = actlRecordTitle.charAt(2);
        switch (moodChar) {
            case 'A':
                actlCorrectAnswer = Mood.ANGER;
                break;
            case 'D':
                actlCorrectAnswer = Mood.DISGUST;
                break;
            case 'F':
                actlCorrectAnswer = Mood.FEAR;
                break;
            case 'J':
                actlCorrectAnswer = Mood.JOY;
                break;
            case 'T':
                actlCorrectAnswer = Mood.SADNESS;
                break;
            case 'S':
                actlCorrectAnswer = Mood.SURPRISE;
                break;
            case 'H':
                actlCorrectAnswer = Mood.NEUTRAL_FAST;
                break;
            case 'N':
                actlCorrectAnswer = Mood.NEUTRAL_SLOW;
                break;
            default:
                // TODO: 8.1.18 Implement exception
                break;
        }
    }

    private String getRandomRecordTitle() {
        int min = 0;
        int max = 0;
        String lng = StorageUtils.loadStringPref(Constants.RECORDS_LANGUAGE, App.getContext());
        if (Constants.RECORDS_LANGUAGE_SI.equals(lng))  {
            min = 337;
            max = numberOfRecords;
        } else if (Constants.RECORDS_LANGUAGE_ENG.equals(lng)) {
            min = 0;
            max = 336;
        }
        int randomNumber = AndroidUtils.getRandomNumber(min, max);

//        FirebaseConnector.sendQuestion(recordsTitles[randomNumber]);

        return recordsTitles[randomNumber];
    }

    public int getTimeAllowedToAnswer() {
        return timeAllowedToAnswer;
    }

    public int getPoints() {
        return points;
    }

    public int getLives() {
        return lives;
    }

    public String getActualLevelAndQuestionInString() {
        return App.getContext().getString(R.string.level) + " " + actlPositionLevel +
                " - " + App.getContext().getString(R.string.question) + " " + actlPositionQuestion +
                "/" + numberOfQuestionsInLevel;
    }

    public String getActlRecordTitle() {
        Log.d(TAG, " --> " + actlRecordTitle);
        return actlRecordTitle;
    }

    public int getActlPositionLevel() {
        return actlPositionLevel;
    }

    public boolean nextQuestion() {
        // there are no more questions end game
        if (gamePlayMode == GamePlayMode.RANKED && actlPositionQuestion == numberOfQuestionsInLevel && actlPositionLevel == numberOfLevels)
            return false;

        actlPositionQuestion += 1;
        if (actlPositionQuestion > numberOfQuestionsInLevel) {
            actlPositionLevel += 1;
            actlPositionQuestion = 1;
        }

        actlRecordTitle = getRandomRecordTitle();
        setActlCorrectAnswer();
        return true;
    }

    public boolean isGameOver() {
        return gamePlayMode == GamePlayMode.RANKED && (actlPositionQuestion == numberOfQuestionsInLevel && actlPositionLevel == numberOfLevels || lives <= 0);
    }

    public int getPrvsAnswerOrder() {
        return prvsAnswerOrder;
    }

    public Mood getPrvsCorrectAnswer() {
        return prvsCorrectAnswer;
    }

    public int getPrvCorrectAnswerOrder() {
        switch (getPrvsCorrectAnswer()) {
            case JOY:
                return 0;
            case ANGER:
                return 1;
            case NEUTRAL_FAST:
                return 2;
            case NEUTRAL_SLOW:
                return 3;
            case SADNESS:
                return 4;
            case SURPRISE:
                return 5;
            case DISGUST:
                return 6;
            case FEAR:
                return 7;
            default:
                // TODO: 8.1.18 add exception
                return -1;
        }
    }

    public int getPrvsEvaluate() {
        return prvsEvaluate;
    }

    public void setGamePlayState(GamePlayState gamePlayState) {
        this.gamePlayState = gamePlayState;
    }

    public GamePlayState getGamePlayState() {
        return gamePlayState;
    }

    public GamePlayMode getGamePlayMode() {
        return gamePlayMode;
    }
}
