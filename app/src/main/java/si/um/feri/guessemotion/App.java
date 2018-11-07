package si.um.feri.guessemotion;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sInstance = this;
    }

    /**
     * Provides app context available for whole app lifecycle.
     */
    @NonNull
    public static Context getContext() {
        return sInstance;
    }

}
