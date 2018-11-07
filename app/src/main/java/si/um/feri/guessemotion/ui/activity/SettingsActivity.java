package si.um.feri.guessemotion.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import si.um.feri.guessemotion.App;
import si.um.feri.guessemotion.R;
import si.um.feri.guessemotion.ui.activity.base.BaseActivity;
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

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.activity_textview_language) TextView languageOption;
    @BindView(R.id.activity_layout_no_user_info) RelativeLayout layoutNoUser;
    @BindView(R.id.activity_layout_user_info) RelativeLayout layoutUser;
    @BindView(R.id.activity_textview_name) TextView nameTextView;
    @BindView(R.id.activity_image_profile) CircleImageView imageView;



    @Override
    public int getLayoutRes() {
        return R.layout.activity_settings;
    }

    @OnClick(R.id.activity_button_back)
    public void onBackButtonClick() {
        onBackPressed();
    }

    @OnClick(R.id.activity_logout_button)
    public void logOutButtonClick() {
//        Firebase log out
        MainMenuActivity.getmAuth().signOut();
        // Google sign out
        MainMenuActivity.getmGoogleSignInClient().signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(SettingsActivity.this, SplashScreenActivity.class);
                        // remove this activity from back stack, then finish it
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainMenuActivity.setFirebaseUser(null);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLanguageBySavedPreference(StorageUtils.loadStringPref(Constants.RECORDS_LANGUAGE, App.getContext()));
        handleUserView();
    }

    private void handleUserView() {
        GoogleSignInAccount googleUser = MainMenuActivity.getGoogleUser();
        nameTextView.setText(googleUser.getDisplayName());
        imageView.setImageURI(googleUser.getPhotoUrl());

        String username = StorageUtils.loadStringPref(Constants.USER_NAME, App.getContext());
//        if (username == null || username.isEmpty()) {
//            layoutNoUser.setVisibility(View.VISIBLE);
//            layoutUser.setVisibility(View.GONE);
//        } else {
//            layoutUser.setVisibility(View.VISIBLE);
//            layoutNoUser.setVisibility(View.GONE);
//        }
    }

    private void setLanguageBySavedPreference(String language) {
        if (language == null || language.isEmpty()) {
            StorageUtils.saveStringPref(Constants.RECORDS_LANGUAGE, Constants.RECORDS_LANGUAGE_SI, App.getContext());
            languageOption.setText(getString(R.string.record_language_si));
        }

        if (Constants.RECORDS_LANGUAGE_SI.equals(language)) {
            languageOption.setText(getString(R.string.record_language_si));
            StorageUtils.saveStringPref(Constants.RECORDS_LANGUAGE, Constants.RECORDS_LANGUAGE_SI, App.getContext());
        } else if (Constants.RECORDS_LANGUAGE_ENG.equals(language)) {
            languageOption.setText(getString(R.string.record_language_english));
            StorageUtils.saveStringPref(Constants.RECORDS_LANGUAGE, Constants.RECORDS_LANGUAGE_ENG, App.getContext());
        }
    }

    @OnClick(R.id.activity_textview_language)
    public void toggleLanguage() {
        String lng = StorageUtils.loadStringPref(Constants.RECORDS_LANGUAGE, App.getContext());
        if (Constants.RECORDS_LANGUAGE_SI.equals(lng)) {
            setLanguageBySavedPreference(Constants.RECORDS_LANGUAGE_ENG);
        } else if (Constants.RECORDS_LANGUAGE_ENG.equals(lng)) {
            setLanguageBySavedPreference(Constants.RECORDS_LANGUAGE_SI);
        }
    }
}
