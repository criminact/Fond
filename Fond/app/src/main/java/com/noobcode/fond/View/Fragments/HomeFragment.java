package com.noobcode.fond.View.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.noobcode.fond.Model.CardStack;
import com.noobcode.fond.Model.User;
import com.noobcode.fond.R;
import com.noobcode.fond.View.HomeActivity;
import com.noobcode.fond.View.OnBoardingActivity;
import com.noobcode.fond.ViewModel.Adapter.CardStackAdapter;
import com.noobcode.fond.ViewModel.CallBacks.CardStackCallBack;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View v;
    FloatingActionButton like;
    FloatingActionButton dislike;
    CardStackLayoutManager manager;
    CardStackAdapter adapter;
    final String TAG = "HomeActivity";
    FirebaseFirestore db;
    List<User> users;
    List<CardStack> items;
    CardStackView cardStackView;
    FloatingActionButton rewind;
    FirebaseUser mUser;
    int i = 0;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        users = new ArrayList<>();
        items = new ArrayList<>();

        getData();

        rewind = (FloatingActionButton) v.findViewById(R.id.rewind);
        like = (FloatingActionButton) v.findViewById(R.id.fablike);
        dislike = (FloatingActionButton) v.findViewById(R.id.fabdislike);

        cardStackView = v.findViewById(R.id.cardStackViewToShow);
        manager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {

                i++;

                if (direction == Direction.Right){
                    //create one way match
                     swipeRight();
                }
                if (direction == Direction.Left){
                    swipeLeft();
                }

                if(manager.getTopPosition() == adapter.getItemCount() - 5) {
                    paginate();
                }

            }

            @Override
            public void onCardRewound() {
                i--;
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.nameFieldText);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.nameFieldText);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }
        });
        manager.setStackFrom(StackFrom.Left);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(12.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.4f);
        manager.setMaxDegree(30.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liked();
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disliked();
            }
        });

        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStackView.rewind();
            }
        });

        return v;
    }

    private void swipeLeft() {
        Runnable runnable = new Runnable() {
            public void run() {
                db.collection("Users").document(mUser.getUid()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                List<String> liked = (List<String>) documentSnapshot.get("liked");
                                if(liked.contains(items.get(i-1).getFirebaseId())){
                                    removeFromLikes();
                                    removeFromLikedBy();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        };

        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    private void removeFromLikedBy(){
        db.collection("Users").document(items.get(i-1).getFirebaseId()).update("likedBy", FieldValue.arrayRemove(mUser.getUid()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot smUser.getUid()uccessfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeFromLikes() {
        db.collection("Users").document(mUser.getUid()).update("liked", FieldValue.arrayRemove(items.get(i-1).getFirebaseId()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void disliked() {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }

    private void liked() {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }

    private void swipeRight() {
        Runnable runnable = new Runnable() {
            public void run() {
                db.collection("Users").document(mUser.getUid()).update("liked", FieldValue.arrayUnion(items.get(i-1).getFirebaseId()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        db.collection("Users").document(items.get(i-1).getFirebaseId()).update("likedBy", FieldValue.arrayUnion(mUser.getUid()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot smUser.getUid()uccessfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        };

        Thread mythread = new Thread(runnable);
        mythread.start();
    }

    private void getData() {
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if(!user.getFirebaseId().equals(mUser.getUid())){
                                    users.add(user);
                                }else if(user.getFirebaseId().equals(mUser.getUid())){
                                    if(!user.getAuth().equals("done")){
                                        Intent intent = new Intent(getContext(), OnBoardingActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }
                            }

                            adapter = new CardStackAdapter(addList(), getContext());
                            cardStackView.setLayoutManager(manager);
                            cardStackView.setAdapter(adapter);
                            cardStackView.setItemAnimator(new DefaultItemAnimator());

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void paginate() {
        List<CardStack> old = adapter.getItems();
        List<CardStack> baru = new ArrayList<>(addList());

        CardStackCallBack cardStackCallBack = new CardStackCallBack(old, baru);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(cardStackCallBack);
        adapter.setItems(baru);
        result.dispatchUpdatesTo(adapter);
    }

    private List<CardStack> addList() {
        for(User user : users){
                items.add(new CardStack(user.getFacebookdp(), user.getNumberOfImages(), user.getName(), Integer.valueOf(user.getAge()), user.getGender(), user.getAddress(), user.getFirebaseId(), user.getBio(), user.getMatchWith(), user.getLookingFor(), user.getImageUris()));
        }
        return items;
    }

}
