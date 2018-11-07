package si.um.feri.guessemotion.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import si.um.feri.guessemotion.R;
import si.um.feri.guessemotion.game.data.enums.GamePlayMode;
import si.um.feri.guessemotion.ui.activity.base.BaseActivity;
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

public class FinalSceneActivity extends BaseActivity {


    @BindView(R.id.activity_final_scene_points_text)
    TextView points_textview;
    @BindView(R.id.activity_final_scene_button_restart)
    ImageButton restart_button;
    @BindView(R.id.activity_final_scene_restart_clicked)
    ImageView restart_button_clicked;
    @BindView(R.id.activity_final_scene_restart_text)
    TextView restart_tw;

    private static final String TAG = "FinalSceneActivity";
    private final String FINAL_POINTS = "FINAL_POINTS";
    private final String GAME_MODE = "GAME_MODE";
    private int points;
    private GamePlayMode gamePlayMode;


    @OnClick(R.id.activity_final_scene_button_restart)
    public void onRestartClick(){
        restart_button.setVisibility(View.INVISIBLE);
        restart_button_clicked.setVisibility(View.VISIBLE);
        restart_tw.setTextColor(Color.RED);
        Intent intent = new Intent(getApplicationContext(), GameSceneActivity.class);
        intent.putExtra(Constants.EXTRA_GAME_PLAY_MODE, gamePlayMode);
        startActivity(intent);
        finish();
    }

    @Override
    protected void getExtras(Intent intent) {
        super.getExtras(intent);
//        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            points = bundle.getInt(FINAL_POINTS);
            gamePlayMode = (GamePlayMode) bundle.get(GAME_MODE);
            points_textview.setText(Integer.toString(points));
        }else{
            points_textview.setText("0000 0000");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_final_scene;
    }

}
