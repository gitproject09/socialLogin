package com.sopan.social_login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.Collections;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    private int googleInRequestCode = 101;
    private boolean socialLogin;

    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(SignInActivity.this);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        callbackManager = CallbackManager.Factory.create();

        findViewById(R.id.google).setOnClickListener(view -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, googleInRequestCode);
        });

        findViewById(R.id.facebook).setOnClickListener(view -> {

            LoginManager.getInstance().logInWithReadPermissions(this, Collections.singletonList("email"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    GraphLoginRequest(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {

                    Toast.makeText(SignInActivity.this, "Login Canceled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {

                    Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e("SignInActivity::", "UserEmail::" + account.getEmail());

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("first_name", account.getEmail());
            intent.putExtra("last_name", "");
            startActivity(intent);

        } catch (Exception e) {
            Log.e("SignInActivity::", "signInResult:failed = " + e);
        }
    }

    // Method to access Facebook User Data.
    protected void GraphLoginRequest(AccessToken accessToken) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken,
                (jsonObject, graphResponse) -> {
                    try {
                        Log.e("SignInActivity::", "UserFacebookFirstName::" + jsonObject.getString("first_name"));
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("first_name", jsonObject.getString("first_name"));
                        intent.putExtra("last_name", jsonObject.getString("last_name"));
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,name,link,email,gender,last_name,first_name,locale,timezone,updated_time,verified");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == googleInRequestCode) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }
}
