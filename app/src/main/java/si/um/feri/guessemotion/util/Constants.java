package si.um.feri.guessemotion.util;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class Constants {

//    database titles of tables
    public static final String DATABASE_APP_INFO = "appInfo";
    public static final String DATABASE_USERS = "users";
    public static final String DATABASE_GAMES = "games";
    public static final String DATABASE_QUESTIONS = "questions";

//    default values for game
    public static final int DEFAULT_NUMBER_OF_LIVES = 20;
    public static final int DEFAULT_TIME_ALLOWED_TO_ANSWER = 20;
    public static final int DEFAULT_NUMBER_OF_QUESTIONS_IN_LEVEL = 20;
    public static final int DEFAULT_NUMBER_OF_LEVELS = 4;

//    titles of field passing via extra between activities
    public static final String EXTRA_GAME_PLAY_MODE = "gamePlayMode";
    public static final String EXTRA_MEDIA_FILE_PATH = "mediaFilePath";
    public static final String EXTRA_MEDIA_ACTUAL_LEVEL = "actualLevel";

    public static final String RECORDS_LANGUAGE = "recordsLng";
    public static final String RECORDS_LANGUAGE_SI = "si";
    public static final String RECORDS_LANGUAGE_ENG = "eng";

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";

    public static final String FIREBASE_TOKEN = "firebaseToken";

}
