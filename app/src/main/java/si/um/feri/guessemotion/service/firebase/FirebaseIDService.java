package si.um.feri.guessemotion.service.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import si.um.feri.guessemotion.App;
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
public class FirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        StorageUtils.saveStringPref(Constants.FIREBASE_TOKEN, refreshedToken, App.getContext());
    }

}