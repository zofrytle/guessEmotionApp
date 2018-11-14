package si.um.feri.guessemotion.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import butterknife.BindView;
import butterknife.OnClick;
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

public class MainMenuActivity extends BaseActivity {

    @BindView(R.id.bmb) BoomMenuButton menuButton;
    @BindView(R.id.signInButton) SignInButton signInButton;
    @BindView(R.id.activity_button_statistics) ImageView statisticsButton;
    @BindView(R.id.activity_button_settings) ImageView settingsButton;

    static private DatabaseReference reference;
    static private GoogleSignInClient mGoogleSignInClient;
    static private FirebaseAuth mAuth;
    static private FirebaseUser firebaseUser = null;
    SharedPreferences prefs;

    static private GoogleSignInAccount googleUser;

    private static final String TAG = "MainMenuActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("users_pref", MODE_PRIVATE);



        TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.play_ranked)
                .imagePadding(new Rect(30, 30, 30, 30))
                .normalText(getString(R.string.app_game_mode_ranked))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(getApplicationContext(), GameSceneActivity.class);
                        intent.putExtra(Constants.EXTRA_GAME_PLAY_MODE,0);
                        startActivity(intent);
                        finish();
                    }
                });
        menuButton.addBuilder(builder);

        builder = new TextInsideCircleButton.Builder()
                .normalImageRes(R.drawable.play_practice)
                .imagePadding(new Rect(30, 30, 30, 30))
                .normalText(getString(R.string.app_game_mode_fun))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(getApplicationContext(), GameSceneActivity.class);
                        intent.putExtra(Constants.EXTRA_GAME_PLAY_MODE,1);
                        startActivity(intent);
                        finish();
                    }
                });
        menuButton.addBuilder(builder);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        boolean login_status = prefs.getBoolean("login_status", false);
        if (login_status) {
            Log.v(TAG, "UserInfo>>User already logged in");
            signIn();
            firebaseUser = mAuth.getCurrentUser();
//            this.finish();
        }

        updateUI(firebaseUser);
        checkLanguage();
        loginUser();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main_menu;
    }

    @OnClick(R.id.activity_button_statistics)
    public void onStatisticsClick() {
        Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.activity_button_settings)
    public void onSettingsClick() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void checkLanguage() {
        // if language is not set, then default value is si
        String language = StorageUtils.loadStringPref(Constants.RECORDS_LANGUAGE, App.getContext());
        if (language == null || language.isEmpty()) {
            StorageUtils.saveStringPref(Constants.RECORDS_LANGUAGE, Constants.RECORDS_LANGUAGE_SI, App.getContext());
        }
    }

    private void loginUser() {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null && !token.isEmpty()) {
            String prvToken = StorageUtils.loadStringPref(Constants.FIREBASE_TOKEN, App.getContext());
            if (!token.equals(prvToken)) StorageUtils.saveStringPref(Constants.FIREBASE_TOKEN, token, App.getContext());
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                googleUser = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(googleUser);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            firebaseUser = mAuth.getCurrentUser();
                            updateUI(firebaseUser);

                            reference = FirebaseDatabase.getInstance().getReference();

                            reference.child("users").child(firebaseUser.getUid()).child("name").setValue(firebaseUser.getDisplayName());
                            reference.child("users").child(firebaseUser.getUid()).child("email").setValue(firebaseUser.getEmail());
                            reference.child("users").child(firebaseUser.getUid()).child("metadata").setValue(firebaseUser.getMetadata());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.activity_failure), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            signInButton.setVisibility(View.GONE);
            menuButton.setVisibility(View.VISIBLE);
            settingsButton.setVisibility(View.VISIBLE);
            statisticsButton.setVisibility(View.VISIBLE);
        } else {
            signInButton.setVisibility(View.VISIBLE);
            menuButton.setVisibility(View.INVISIBLE);
            settingsButton.setVisibility(View.INVISIBLE);
            statisticsButton.setVisibility(View.INVISIBLE);
        }
    }
    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 123);

        SharedPreferences.Editor editor = getSharedPreferences("users_pref", MODE_PRIVATE).edit();
        editor.putBoolean("login_status", true);
        editor.apply();

    }
    static public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    static public void setFirebaseUser (FirebaseUser user){ firebaseUser = user; }

    static public GoogleSignInAccount getGoogleUser() {
        return googleUser;
    }

    static public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    static public FirebaseAuth getmAuth() {
        return mAuth;
    }

    static public DatabaseReference getReference() {return reference;}
}
