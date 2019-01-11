package si.um.feri.guessemotion.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        Intent intent = new Intent(this, MainMenuActivity.class );
        startActivity(intent);
        finish();
    }



}
