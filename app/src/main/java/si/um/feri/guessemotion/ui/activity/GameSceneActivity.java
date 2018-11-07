package si.um.feri.guessemotion.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ohoussein.playpause.PlayPauseView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import si.um.feri.guessemotion.R;
import si.um.feri.guessemotion.game.core.GamePlay;
import si.um.feri.guessemotion.game.data.domain.Game;
import si.um.feri.guessemotion.game.data.enums.GamePlayMode;
import si.um.feri.guessemotion.game.data.enums.GamePlayState;
import si.um.feri.guessemotion.service.GPSTracker;
import si.um.feri.guessemotion.service.MediaPlayerService;
import si.um.feri.guessemotion.service.MediaPlayerServiceCallback;
import si.um.feri.guessemotion.service.firebase.FirebaseConnector;
import si.um.feri.guessemotion.ui.activity.base.BaseActivity;
import si.um.feri.guessemotion.ui.widget.ProgressCircleBar;
import si.um.feri.guessemotion.util.AndroidUtils;
import si.um.feri.guessemotion.util.Constants;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class GameSceneActivity extends BaseActivity implements MediaPlayerServiceCallback, ProgressCircleBar.ProgressAnimationListener {

    @BindView(R.id.activity_layout_downside)
    LinearLayout layoutDownside;
    @BindView(R.id.activity_progress_bar)
    ProgressCircleBar progressCircleBar;
    @BindView(R.id.activity_button_play)
    PlayPauseView playPauseView;

    @BindView(R.id.activity_relative_layout_joy)
    RelativeLayout rlayoutJoy;
    @BindView(R.id.activity_relative_layout_anger)
    RelativeLayout rlayoutAnger;
    @BindView(R.id.activity_relative_layout_fast)
    RelativeLayout rlayoutFast;
    @BindView(R.id.activity_relative_layout_slow)
    RelativeLayout rlayoutSlow;
    @BindView(R.id.activity_relative_layout_sadness)
    RelativeLayout rlayoutSadness;
    @BindView(R.id.activity_relative_layout_surprise)
    RelativeLayout rlayoutSurprise;
    @BindView(R.id.activity_relative_layout_disgust)
    RelativeLayout rlayoutDiscuss;
    @BindView(R.id.activity_relative_layout_fear)
    RelativeLayout rlayoutFear;

    @BindView(R.id.activity_linear_layout_joy)
    LinearLayout layoutJoyContainer;
    @BindView(R.id.activity_linear_layout_anger)
    LinearLayout layoutAngerContainer;
    @BindView(R.id.activity_linear_layout_fast)
    LinearLayout layoutFastContainer;
    @BindView(R.id.activity_linear_layout_slow)
    LinearLayout layoutSlowContainer;
    @BindView(R.id.activity_linear_layout_sadness)
    LinearLayout layoutSadnessContainer;
    @BindView(R.id.activity_linear_layout_surprise)
    LinearLayout layoutSurpriseContainer;
    @BindView(R.id.activity_linear_layout_disgust)
    LinearLayout layoutDiscussContainer;
    @BindView(R.id.activity_linear_layout_fear)
    LinearLayout layoutFearContainer;

    @BindView(R.id.activity_image_joy)
    ImageView imageJoy;
    @BindView(R.id.activity_image_anger)
    ImageView imageAnger;
    @BindView(R.id.activity_image_nfast)
    ImageView imageFast;
    @BindView(R.id.activity_image_nslow)
    ImageView imageSlow;
    @BindView(R.id.activity_image_sadness)
    ImageView imageSadness;
    @BindView(R.id.activity_image_surprise)
    ImageView imageSurprise;
    @BindView(R.id.activity_image_disgust)
    ImageView imageDisgust;
    @BindView(R.id.activity_image_fear)
    ImageView imageFear;

    @BindView(R.id.activity_text_joy)
    TextView textViewJoy;
    @BindView(R.id.activity_text_anger)
    TextView textViewAnger;
    @BindView(R.id.activity_text_nfast)
    TextView textViewFast;
    @BindView(R.id.activity_text_nslow)
    TextView textViewSlow;
    @BindView(R.id.activity_text_sadness)
    TextView textViewSadness;
    @BindView(R.id.activity_text_surprise)
    TextView textViewSurprise;
    @BindView(R.id.activity_text_disgust)
    TextView textViewDisgust;
    @BindView(R.id.activity_text_fear)
    TextView textViewFear;

    @BindView(R.id.activity_text_heart)
    TextView livesText;
    @BindView(R.id.activity_text_points)
    TextView pointsText;
    @BindView(R.id.activity_text_levelinfo)
    TextView levelinfoText;
    @BindView(R.id.activity_image_heart)
    ImageView livesImage;

    private static final String TAG = "GameSceneActivity";

    private boolean aswersButtonClickability;
    private boolean playButtonClickability;
    private boolean mIsPlaying = false;
    private boolean mFirst = false;
    private int length;
    private int duration;
    private GamePlay gamePlay;

    private RelativeLayout[] moodLayouts = new RelativeLayout[8];
    private LinearLayout[] moodContainers = new LinearLayout[8];

    private GPSTracker gpsTracker;
    private MediaPlayerService player;
    boolean serviceBound = false;

    private FirebaseConnector firebaseConnector;
    private Game game;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        gpsTracker = new GPSTracker(this);

//        if (!gpsTracker.isGPSEnabled()) {
//            gpsTracker.showSettingsAlert();
//        }


        initializeLayoutsAndContainers();
        initViewsForGame();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_game_scene;
    }

    @Override
    protected void getExtras(Intent intent) {
        if (intent != null) {
            // initialize of game play
            int gamePlayMode = intent.getIntExtra(Constants.EXTRA_GAME_PLAY_MODE, 0);

            gamePlay = new GamePlay(this, gamePlayMode == 0 ? GamePlayMode.RANKED : GamePlayMode.PRACTICE);
            System.out.println("------------------->>> "+ gamePlay.getGamePlayMode().toString());
        }
    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
            player.setCallbacks(GameSceneActivity.this);

            Log.d(TAG, "Media Player Service is bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void playAudio(String media) {
        //Check is service is active
        playPauseView.toggle();
        if (!serviceBound) {
            gamePlay.setGamePlayState(GamePlayState.WAITING_FOR_ANSWER);
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            playerIntent.putExtra(Constants.EXTRA_MEDIA_FILE_PATH, media);
            playerIntent.putExtra(Constants.EXTRA_GAME_PLAY_MODE, gamePlay.getGamePlayMode() == GamePlayMode.RANKED ? 0 : 1);
            playerIntent.putExtra(Constants.EXTRA_MEDIA_ACTUAL_LEVEL, gamePlay.getActlPositionLevel());
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            if (player.isPlaying()) {
                gamePlay.setGamePlayState(GamePlayState.WAITING_FOR_PLAYING_RECORD);
                player.pauseMedia();
            } else {
                if (gamePlay.getGamePlayState() == GamePlayState.WAITING_FOR_PLAYING_RECORD) {
                    player.playNextRecord(gamePlay.getActlRecordTitle(), gamePlay.getActlPositionLevel());
                } else {
                    player.resumeMedia();
                }
                gamePlay.setGamePlayState(GamePlayState.WAITING_FOR_ANSWER);
            }
            //Service is active
            //Send media with BroadcastReceiver
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            player.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }

    private void initializeLayoutsAndContainers() {
        moodLayouts[0] = rlayoutJoy;
        moodLayouts[1] = rlayoutAnger;
        moodLayouts[2] = rlayoutFast;
        moodLayouts[3] = rlayoutSlow;
        moodLayouts[4] = rlayoutSadness;
        moodLayouts[5] = rlayoutSurprise;
        moodLayouts[6] = rlayoutDiscuss;
        moodLayouts[7] = rlayoutFear;

        moodContainers[0] = layoutJoyContainer;
        moodContainers[1] = layoutAngerContainer;
        moodContainers[2] = layoutFastContainer;
        moodContainers[3] = layoutSlowContainer;
        moodContainers[4] = layoutSadnessContainer;
        moodContainers[5] = layoutSurpriseContainer;
        moodContainers[6] = layoutDiscussContainer;
        moodContainers[7] = layoutFearContainer;
    }

    private void initViewsForGame() {
        progressCircleBar.setMax(gamePlay.getTimeAllowedToAnswer());
        progressCircleBar.animateProgressTo(gamePlay.getTimeAllowedToAnswer(), 0, this);

        if (gamePlay.getGamePlayMode() == GamePlayMode.PRACTICE) {
            levelinfoText.setVisibility(View.GONE);
            livesText.setVisibility(View.GONE);
            livesImage.setVisibility(View.GONE);
        } else if (gamePlay.getGamePlayMode() == GamePlayMode.RANKED) {
            levelinfoText.setVisibility(View.VISIBLE);
            livesText.setVisibility(View.VISIBLE);
            livesImage.setVisibility(View.VISIBLE);

            firebaseConnector = new FirebaseConnector();

            game = new Game(MainMenuActivity.getFirebaseUser());

            firebaseConnector.sendGame(game.getUid());
        }

        pointsText.setText(String.format(Locale.getDefault(), getString(R.string.points_shortcut), gamePlay.getPoints()));
        livesText.setText(String.format(Locale.getDefault(), "%d", gamePlay.getLives()));
        levelinfoText.setText(gamePlay.getActualLevelAndQuestionInString());
        toggleAnswerButtonClickable(false);
        togglePlayPauseButtonClickable(true);
    }

    @OnClick(R.id.activity_button_back)
    public void onBackButtonClick() {
        new MaterialDialog.Builder(this)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                }).title(getString(R.string.exit_dialog_title))
                .content(gamePlay.getGamePlayMode() == GamePlayMode.RANKED ?
                        getString(R.string.exit_dialog_desc_ranked) : getString(R.string.exit_dialog_desc_practise))
                .positiveText(getString(R.string.exit_dialog_yes))
                .negativeText(getString(R.string.exit_dialog_no))
                .show();
    }

    @OnClick(R.id.activity_button_play)
    public void onPlayPauseViewClick() {
        if (!playButtonClickability) return;
        if (gamePlay.getGamePlayState() == GamePlayState.WAITING_FOR_PLAYING_RECORD)
            playAudio(gamePlay.getActlRecordTitle());
        else handleNextLevel();
    }

    @OnClick(R.id.activity_relative_layout_joy)
    public void onJoyLayoutClick() {
        if (aswersButtonClickability) answerSelected(0);
    }

    @OnClick(R.id.activity_relative_layout_anger)
    public void onAngerLayoutClick() {
        if (aswersButtonClickability) answerSelected(1);
    }

    @OnClick(R.id.activity_relative_layout_fast)
    public void onFastLayoutClick() {
        if (aswersButtonClickability) answerSelected(2);
    }

    @OnClick(R.id.activity_relative_layout_slow)
    public void onSlowLayoutClick() {
        if (aswersButtonClickability) answerSelected(3);
    }

    @OnClick(R.id.activity_relative_layout_sadness)
    public void onSadnessLayoutClick() {
        if (aswersButtonClickability) answerSelected(4);
    }

    @OnClick(R.id.activity_relative_layout_surprise)
    public void onSurpriseLayoutClick() {
        if (aswersButtonClickability) answerSelected(5);
    }

    @OnClick(R.id.activity_relative_layout_disgust)
    public void onDiscussLayoutClick() {
        if (aswersButtonClickability) answerSelected(6);
    }

    @OnClick(R.id.activity_relative_layout_fear)
    public void onFearLayoutClick() {
        if (aswersButtonClickability) answerSelected(7);
    }

    private void handleNextLevel() {
//        boolean isNext = gamePlay.nextQuestion() ;
        if (gamePlay.nextQuestion()) {
            if (gamePlay.getGamePlayMode() == GamePlayMode.RANKED) {
                levelinfoText.setText(gamePlay.getActualLevelAndQuestionInString());
                TransitionManager.beginDelayedTransition(layoutDownside, new Slide(Gravity.RIGHT));
                int visibleValue = levelinfoText.getVisibility();
                levelinfoText.setVisibility(visibleValue == 4 ? View.VISIBLE : View.INVISIBLE);
            }
            prepareForNextQuestion();
        } else {
            if (gamePlay.isGameOver()) {
                gameOver();
            }
        }
    }

    private void gameOver() {
        Intent intent = new Intent(getApplicationContext(), FinalSceneActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("GAME_MODE",gamePlay.getGamePlayMode());
        bundle.putInt("FINAL_POINTS", gamePlay.getPoints());
//        intent.putExtra("FINAL_POINTS", gamePlay.getPoints());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void handleEvaluation(int evaluateResult) {
        if (gamePlay.isGameOver()) {
            gameOver();
        }
        updateViews();
        switch (evaluateResult) {
            // run out of the time
            case -1:
                onOutOfTime();
                break;
            // correct answer
            case 0:
                onCorrectAnswer();
                break;
            // wrong answer
            case 1:
                onWrongAnswer();
                break;
        }
        firebaseConnector.sendQuestion(gamePlay.getActualLevelAndQuestionInString());
        firebaseConnector.sendResult(evaluateResult);
    }

    private void updateViews() {
        progressCircleBar.setProgress(gamePlay.getTimeAllowedToAnswer());
        pointsText.setText(String.format(Locale.getDefault(), getString(R.string.points_shortcut), gamePlay.getPoints()));
        livesText.setText(String.format(Locale.getDefault(), "%d", gamePlay.getLives()));
        toggleAnswerButtonClickable(false);
        togglePlayPauseButtonClickable(true);
    }

    private void onCorrectAnswer() {
        progressCircleBar.setTitle(getString(R.string.success));
        progressCircleBar.setSubTitle(getString(R.string.correct));
        paintProgressCircleBar(ContextCompat.getColor(this, R.color.colorGreen));
        ImageView correctImage = getMoodImageViewByPosition(gamePlay.getPrvsAnswerOrder());
        TextView correctTextView = getMoodTextViewByPosition(gamePlay.getPrvsAnswerOrder());
        correctTextView.setAlpha(1f);
        correctTextView.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
        correctImage.setAlpha(1f);
        correctImage.setImageResource(AndroidUtils.getCorrectDrawableByPosition(gamePlay.getPrvsAnswerOrder()));
    }

    private void onWrongAnswer() {
        progressCircleBar.setTitle(getString(R.string.failure));
        progressCircleBar.setSubTitle(getString(R.string.wrong));
        paintProgressCircleBar(ContextCompat.getColor(this, R.color.colorRed));
        ImageView wrongImage = getMoodImageViewByPosition(gamePlay.getPrvsAnswerOrder());
        TextView wrongTextView = getMoodTextViewByPosition(gamePlay.getPrvsAnswerOrder());
        wrongTextView.setAlpha(1f);
        wrongTextView.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        wrongImage.setAlpha(1f);
        wrongImage.setImageResource(AndroidUtils.getWrongDrawableByPosition(gamePlay.getPrvsAnswerOrder()));
        ImageView correctImage = getMoodImageViewByPosition(gamePlay.getPrvCorrectAnswerOrder());
        TextView correctTextView = getMoodTextViewByPosition(gamePlay.getPrvCorrectAnswerOrder());
        correctTextView.setAlpha(1f);
        correctTextView.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
        correctImage.setAlpha(1f);
        correctImage.setImageResource(AndroidUtils.getCorrectDrawableByPosition(gamePlay.getPrvCorrectAnswerOrder()));
    }

    private void onOutOfTime() {
        progressCircleBar.setTitle(getString(R.string.failure));
        progressCircleBar.setSubTitle(getString(R.string.out_of_time));
        paintProgressCircleBar(ContextCompat.getColor(this, R.color.colorRed));
        ImageView correctImage = getMoodImageViewByPosition(gamePlay.getPrvCorrectAnswerOrder());
        TextView correctTextView = getMoodTextViewByPosition(gamePlay.getPrvCorrectAnswerOrder());
        correctTextView.setAlpha(1f);
        correctTextView.setTextColor(ContextCompat.getColor(this, R.color.colorGreen));
        correctImage.setAlpha(1f);
        correctImage.setImageResource(AndroidUtils.getCorrectDrawableByPosition(gamePlay.getPrvCorrectAnswerOrder()));
    }

    private void paintProgressCircleBar(int color) {
        progressCircleBar.setTitleColor(color);
        progressCircleBar.setSubTitleColor(color);
        progressCircleBar.setProgresBarColor(color);
    }

    private void answerSelected(int answerPosition) {
        toggleAnswerButtonClickable(false);
        togglePlayPauseButtonClickable(false);
        if (player.isPlaying()) {
            player.stopSelf();
        }
        animateDownsideLayout();
        progressCircleBar.pauseAnimation();
        fadeNonCheckAnswers(answerPosition);
        handleEvaluation(gamePlay.evaluateQuestion(answerPosition));
    }

    private void animateDownsideLayout() {
        if (gamePlay.getGamePlayMode() == GamePlayMode.PRACTICE) return;
        TransitionManager.beginDelayedTransition(layoutDownside, new Slide(Gravity.RIGHT));
        int visibleValue = levelinfoText.getVisibility();
        levelinfoText.setVisibility(visibleValue == 4 ? View.VISIBLE : View.INVISIBLE);
    }

    private void fadeNonCheckAnswers(int chooseItem) {
        for (int i = 0; i < 8; i++) {
            if (i != chooseItem) {
                ImageView imageView = getMoodImageViewByPosition(i);
                TextView textView = getMoodTextViewByPosition(i);
                imageView.setAlpha(0.15f);
                textView.setAlpha(0.15f);
            }
        }
    }

    private ImageView getMoodImageViewByPosition(int position) {
        switch (position) {
            case 0:
                return imageJoy;
            case 1:
                return imageAnger;
            case 2:
                return imageFast;
            case 3:
                return imageSlow;
            case 4:
                return imageSadness;
            case 5:
                return imageSurprise;
            case 6:
                return imageDisgust;
            case 7:
                return imageFear;
            default:
                return null;
        }
    }

    private TextView getMoodTextViewByPosition(int position) {
        switch (position) {
            case 0:
                return textViewJoy;
            case 1:
                return textViewAnger;
            case 2:
                return textViewFast;
            case 3:
                return textViewSlow;
            case 4:
                return textViewSadness;
            case 5:
                return textViewSurprise;
            case 6:
                return textViewDisgust;
            case 7:
                return textViewFear;
            default:
                return null;
        }
    }

    private void toggleAnswerButtonClickable(boolean clickable) {
        rlayoutJoy.setClickable(clickable);
        rlayoutAnger.setClickable(clickable);
        rlayoutFast.setClickable(clickable);
        rlayoutSlow.setClickable(clickable);
        rlayoutSadness.setClickable(clickable);
        rlayoutSurprise.setClickable(clickable);
        rlayoutDiscuss.setClickable(clickable);
        rlayoutFear.setClickable(clickable);
        aswersButtonClickability = clickable;
    }

    private void togglePlayPauseButtonClickable(boolean clickable) {
        playPauseView.setClickable(clickable);
        playButtonClickability = clickable;
    }

    private void prepareForNextQuestion() {
        gamePlay.setGamePlayState(GamePlayState.WAITING_FOR_PLAYING_RECORD);
        progressCircleBar.setTitle(String.valueOf(gamePlay.getTimeAllowedToAnswer()));
        progressCircleBar.setTitleColor(ContextCompat.getColor(this, R.color.progress_circle_default_title));
        progressCircleBar.setSubTitle("");
        paintProgressCircleBar(ContextCompat.getColor(this, R.color.progress_circle_default_progress));
        for (int i = 0; i < 8; i++) {
            ImageView imageView = getMoodImageViewByPosition(i);
            TextView textView = getMoodTextViewByPosition(i);
            textView.setAlpha(1f);
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            imageView.setAlpha(1f);
            imageView.setImageResource(AndroidUtils.getDrawableByPosition(i));
        }
    }

    @Override
    public void onFinishRecord() {
        Log.d(TAG, "onFinishRecord fired!");
        playPauseView.toggle();
        togglePlayPauseButtonClickable(false);
        toggleAnswerButtonClickable(true);
        progressCircleBar.startAnimation();
    }

    @Override
    public void onAnimationStart() {

    }

    @Override
    public void onAnimationFinish() {
        Log.d(TAG, "onAnimationFinish fired!");
        animateDownsideLayout();
        toggleAnswerButtonClickable(false);
        togglePlayPauseButtonClickable(false);
        progressCircleBar.setProgress(gamePlay.getTimeAllowedToAnswer());
        fadeNonCheckAnswers(-1);
        handleEvaluation(gamePlay.evaluateQuestion(-1));
    }

    @Override
    public void onAnimationProgress(int progress) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}

