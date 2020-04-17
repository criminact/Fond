package com.noobcode.fond.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.noobcode.fond.Model.Chat;
import com.noobcode.fond.Model.Message;
import com.noobcode.fond.R;
import com.noobcode.fond.ViewModel.Adapter.ChatAdapter;
import com.noobcode.fond.ViewModel.Adapter.MessageAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    final String TAG = "MessageActivity";
    List<Message> messages1;
    FirebaseUser mUser;
    TextView nameField;
    ImageView profileField;
    FirebaseFirestore db;
    List<String> messages;
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;
    EditText message;
    ImageButton send;
    DocumentReference reference;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        db = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        messages1 = new ArrayList<>();
        messages = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager Ly = new LinearLayoutManager(MessageActivity.this);
        Ly.setStackFromEnd(true);
        recyclerView.setLayoutManager(Ly);
        messageAdapter = new MessageAdapter();

        getMessages();

        message = (EditText) findViewById(R.id.messageSender);
        send = (ImageButton) findViewById(R.id.sender);
        nameField = (TextView) findViewById(R.id.nameinMessage);
        profileField = (ImageView) findViewById(R.id.profileinImage);

        nameField.setText(getIntent().getStringExtra("name"));
        Picasso.get().load(Uri.parse(getIntent().getStringExtra("profile"))).fit().into(profileField);

        //get Messages

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message.getText().toString().trim().isEmpty()){
                    Toast.makeText(MessageActivity.this, "Can't send an empty message.", Toast.LENGTH_SHORT).show();
                }else{
                    String mess = message.getText().toString().trim();
                    message.setText("");
                    Runnable runnable = new Runnable() {
                        public void run() {
                            sendMessage(mess, getIntent().getStringExtra("id"), mUser.getUid(), String.valueOf(messages1.size() + 1));
                        }
                    };

                    Thread mythread = new Thread(runnable);
                    mythread.start();
                }
            }
        });

        //set messages
    if(i == 0) {
        reference = db.collection("Users").document(mUser.getUid()).collection("Chats").document(getIntent().getStringExtra("id"));
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    messages = (List<String>) documentSnapshot.get("messages");

                    if (messages.size() > 0) {
                        for (String message : messages) {
                            String st[] = message.split("@", 4);

                            //if(st[2] == mUser.getUid()) {
                                messages1.add(new Message(st[0], st[1], st[2], st[3]));
                                messageAdapter = new MessageAdapter(MessageActivity.this, messages1);
                                recyclerView.setAdapter(messageAdapter);
                            //}
                        }
                    }


                } else {
                    System.out.print("Current data: null");
                }
            }
        });


        i = 1;

    }else{
        reference = db.collection("Users").document(getIntent().getStringExtra("id")).collection("Chats").document(mUser.getUid());
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    messages = (List<String>) documentSnapshot.get("messages");

                    if (messages.size() > 0) {
                        for (String message : messages) {
                            String st[] = message.split("@", 4);

                            //if(st[2] == mUser.getUid()) {
                                messages1.add(new Message(st[0], st[1], st[2], st[3]));
                                messageAdapter = new MessageAdapter(MessageActivity.this, messages1);
                                recyclerView.setAdapter(messageAdapter);
                            //}
                        }
                    }

                } else {
                    System.out.print("Current data: null");
                }
            }
        });

        i = 0;
    }
}
        //create chatting capabilities

    private void sendMessage(String message, String sentto, String sentby, String number) {
        setMessageSelf(message, sentto, sentby, number);
        setMessageUser(message, sentto, sentby, number);
    }

    private void setMessageUser(String messages, String sentto, String sentby, String number) {

        Map<String, Object> map = new HashMap<>();
        map.put("messages", FieldValue.arrayUnion(messages + "@" + number + "@" + sentby + "@" + sentto));
        map.put("count", messages.length());

        db.collection("Users").document(getIntent().getStringExtra("id")).collection("Chats").document(mUser.getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           // message.setText("");
                        } else {
                            Toast.makeText(MessageActivity.this, "Failed to send", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void setMessageSelf(String messages, String sentto, String sentby, String number) {

        Map<String, Object> map = new HashMap<>();
        map.put("messages", FieldValue.arrayUnion(messages + "@" + number + "@" + sentby + "@" + sentto));
        map.put("count", messages.length());

        db.collection("Users").document(mUser.getUid()).collection("Chats").document(getIntent().getStringExtra("id")).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //reset data
                            //messages1.add(new Message(messages, number,  sentby, sentto));
                            //add data
                            //messageAdapter = new MessageAdapter(MessageActivity.this, messages1);
                            //adapter
                            //recyclerView.setAdapter(messageAdapter);
                            //show
                            //messageAdapter.notifyDataSetChanged();
                            //message.setText("");
                        } else {
                            Toast.makeText(MessageActivity.this, "Failed to send", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void getMessages() {
        db.collection("Users").document(mUser.getUid()).collection("Chats").document(getIntent().getStringExtra("id"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            messages = (List<String>) document.get("messages");

                           if(messages.size() > 0) {
                               for (String message : messages) {
                                   String st[] = message.split("@", 4);
                                   messages1.add(new Message(st[0], st[1], st[2], st[3]));
                               }
                               messageAdapter = new MessageAdapter(MessageActivity.this, messages1);
                               recyclerView.setAdapter(messageAdapter);
                           }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
