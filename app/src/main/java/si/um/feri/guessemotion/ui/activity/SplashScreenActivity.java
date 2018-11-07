package si.um.feri.guessemotion.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

import si.um.feri.guessemotion.R;
import si.um.feri.guessemotion.ui.activity.base.BaseActivity;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class SplashScreenActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                login();
            }
        }, 100); // show splash screen
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash_screen;
    }

    private void login(){
        Intent intent = new Intent(SplashScreenActivity.this, MainMenuActivity.class);
        // remove this activity from back stack, then finish it
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}
