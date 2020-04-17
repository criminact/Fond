package com.noobcode.fond.View.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.noobcode.fond.Model.Chat;
import com.noobcode.fond.Model.Match;
import com.noobcode.fond.Model.Message;
import com.noobcode.fond.R;
import com.noobcode.fond.View.MessageActivity;
import com.noobcode.fond.ViewModel.Adapter.ChatAdapter;
import com.noobcode.fond.ViewModel.Adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    View v;
    FirebaseFirestore db;
    FirebaseUser mUser;
    List<Chat> chats;
    List<String> liked;
    List<String> likedBy;
    final String TAG = "HomeActivity";
    List<String> matches;
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    List<String>  messages;
    int i = 0;
    DocumentReference reference;


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_chat, container, false);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        recyclerView = v.findViewById(R.id.chatView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //progressBar = (ProgressBar) v.findViewById(R.id.progressbarinchats);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        chatAdapter = new ChatAdapter();

        chats = new ArrayList<>();
        messages = new ArrayList<>();
        matches = new ArrayList<>();
        liked = new ArrayList<>();
        likedBy = new ArrayList<>();

        getData();

        reference = db.collection("Users").document(mUser.getUid()).collection("Chats").document(mUser.getUid());
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {

                    liked = (List<String>) documentSnapshot.get("liked");
                    likedBy = (List<String>) documentSnapshot.get("likedBy");

                    Match match = new Match(liked, likedBy);
                    matches = match.getMatches();

                    getMessageData();


                } else {
                    System.out.print("Current data: null");
                }
            }
        });


        return v;
    }

    private void attachListener() {

    }

    private void getData() {
        db.collection("Users").document(mUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            liked = (List<String>) document.get("liked");
                            likedBy = (List<String>) document.get("likedBy");

                            Match match = new Match(liked, likedBy);
                            matches = match.getMatches();

                            getMessageData();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getMessageData() {
       if(matches != null) {
           for (String id : matches) {
               db.collection("Users").document(id)
                       .get()
                       .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               if (task.isSuccessful()) {
                                   DocumentSnapshot document = task.getResult();

                                   String facebookdp = document.getString("facebookdp");
                                   String name = document.getString("name");
                                   String id = document.getString("firebaseId");

                                   getMessages(id);

                                   chats.add(new Chat(facebookdp, name, id));
                                   chatAdapter = new ChatAdapter(chats, v.getContext());
                                   recyclerView.setAdapter(chatAdapter);

                               } else {
                                   Log.d(TAG, "Error getting documents: ", task.getException());
                               }
                           }
                       });
           }
       }
    }

    private void getMessages(String id) {

        db.collection("Users").document(mUser.getUid()).collection("Chats").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            messages = (List<String>) document.get("messages");

                                if(messages != null && messages.size() != 0){
                                    makeChat(id);
                                }else{
                                    makeChat3(id);
                                }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void makeChat(String id) {

        Map<String, Object> data = new HashMap<>();
        data.put("messages", messages);

        db.collection("Users").document(mUser.getUid()).collection("Chats").document(id)
                .update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               makeChat2(id, data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to make chat.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void makeChat2(String id, Map<String, Object> data) {

        db.collection("Users").document(id).collection("Chats").document(mUser.getUid())
                .update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               //
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to make chat 2.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void makeChat3(String id) {

        messages = new ArrayList<>();

        Map<String, Object> data = new HashMap<>();
        data.put("messages", messages);

        db.collection("Users").document(mUser.getUid()).collection("Chats").document(id)
                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                makeChat4(id, data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to make chat 3 .", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void makeChat4(String id, Map<String, Object> data) {

        db.collection("Users").document(id).collection("Chats").document(mUser.getUid())
                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to make chat 4.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
