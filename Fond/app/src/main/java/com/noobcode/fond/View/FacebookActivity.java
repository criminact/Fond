package com.noobcode.fond.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.noobcode.fond.R;

import org.json.JSONArray;
import org.json.JSONException;

public class FacebookActivity extends AppCompatActivity {

    CallbackManager mCallbackManager;
    FirebaseAuth mAuth;
    String name;
    String id;
    String facebookdp;
    CardView cardView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);

        mAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        cardView = (CardView) findViewById(R.id.cardViewToHide);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile", "user_photos", "user_friends", "user_birthday", "user_gender");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                cardView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookActivity.this, "Authentication was cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(FacebookActivity.this, "Recieved the following error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Profile profile = Profile.getCurrentProfile();
                            if(profile != null){
                                 name = profile.getName();
                                 id = profile.getId();
                                 facebookdp = profile.getProfilePictureUri(300, 300).toString();
                            }

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {

                                updateUIForNewUser(user, name, id, facebookdp, mAuth.getCurrentUser().getUid());
                            } else {

                                updateUI(user, name, id, facebookdp);
                            }
                        } else {
                            Toast.makeText(FacebookActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null, null, null , null);
                        }
                    }
                });
    }

    private void updateUIForNewUser(FirebaseUser user, String name, String id, String facebookdp, String Fid) {
       if(user == null){
           Toast.makeText(this, "Couldn't Login, Please try Logging in again.", Toast.LENGTH_SHORT).show();
       } else {
           Intent intent = new Intent(FacebookActivity.this, OnBoardingActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
           intent.putExtra("userId", user.getUid());
           intent.putExtra("name", name);
           intent.putExtra("id", id);
           intent.putExtra("facebookdp", facebookdp);
  //         intent.putExtra("friendlist", data);
           startActivity(intent);
       }
    }

    private void updateUI(FirebaseUser user, String name, String id, String facebookdp) {
        if (user == null) {
            Toast.makeText(this, "Couldn't Login, Please try Logging in again.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(FacebookActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("user", user);
            intent.putExtra("name", name);
            intent.putExtra("id", id);
            intent.putExtra("facebookdp", facebookdp);
//            intent.putExtra("friendlist", data);
            startActivity(intent);
        }
    }
}
