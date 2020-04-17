package com.noobcode.fond.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.noobcode.fond.R;
import com.noobcode.fond.ViewModel.FirebaseAuthUtils;

public class MainActivity extends AppCompatActivity {

    FirebaseAuthUtils firebaseAuthUtils;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuthUtils = new FirebaseAuthUtils(mUser);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuthUtils.isLoggedin()){
            updateUiForOldUser();
        }else{
            updateUiForNewUser();
        }
    }

    private void updateUiForNewUser() {
        Intent intent = new Intent(MainActivity.this, FacebookActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateUiForOldUser() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
