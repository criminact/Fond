package com.noobcode.fond.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthUtils {

    FirebaseUser mUser;
    FirebaseAuth mAuth;

    public FirebaseAuthUtils(FirebaseUser mUser, FirebaseAuth mAuth) {
        this.mUser = mUser;
        this.mAuth = mAuth;
    }

    public FirebaseAuthUtils(FirebaseUser mUser) {
        this.mUser = mUser;
    }

    public FirebaseAuthUtils() {
    }

    public FirebaseUser makeUser(){
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        return mUser;
    }

    public boolean isLoggedin(){
        if(this.mUser == null){
            return false;
        }else{
            return true;
        }
    }


}
