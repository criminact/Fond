package com.noobcode.fond.View.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.noobcode.fond.Model.User;
import com.noobcode.fond.R;
import com.noobcode.fond.View.FacebookActivity;
import com.noobcode.fond.ViewModel.Adapter.CardStackAdapter;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

 View v;
 FirebaseFirestore db;
 FirebaseUser mUser;
 String name;
 String age;
 String profile;
 String bio;

 EditText nameField;
 EditText bioField;
 EditText ageField;
 ImageView profileField;


 Button save;
 //Button logout;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        nameField = (EditText) v.findViewById(R.id.nameinprofile);
        bioField = (EditText) v.findViewById(R.id.bioinprofile);
        ageField = (EditText) v.findViewById(R.id.ageinprofile);
        profileField = (ImageView) v.findViewById(R.id.profileinprofile);
        save = (Button) v.findViewById(R.id.savedetails);
        //logout = (Button) v.findViewById(R.id.logout);

        getData();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });

  /*      logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FacebookActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
*/
        return v;
    }


    private void setData() {
        if(ageField.getText().toString().trim().isEmpty() || nameField.getText().toString().trim().isEmpty() || bioField.getText().toString().trim().isEmpty()){
            Toast.makeText(v.getContext(), "Something seems empty, please check again.", Toast.LENGTH_SHORT).show();
        }else{
            Map<String, Object> details = new HashMap<>();
            details.put("age", ageField.getText().toString().trim());
            details.put("name", nameField.getText().toString().trim());
            details.put("bio", bioField.getText().toString().trim());

            db.collection("Users").document(mUser.getUid()).update(details)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Changes successfully saved.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }

    private void getData() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            name = documentSnapshot.getString("name");
                            nameField.setText(name);
                            bio = documentSnapshot.getString("bio");
                            bioField.setText(bio);
                            age = documentSnapshot.getString("age");
                            ageField.setText(age);
                            profile = documentSnapshot.getString("facebookdp");
                            Picasso.get().load(Uri.parse(profile)).centerCrop().fit().into(profileField);

                        } else {
                            Toast.makeText(getContext(), "Can't get data in profile.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
