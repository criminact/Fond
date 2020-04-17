package com.noobcode.fond.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.noobcode.fond.Model.User;
import com.noobcode.fond.R;
import com.noobcode.fond.ViewModel.AskingRuntimePermissions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoActivity extends AppCompatActivity {

    Dialog Imagedialog;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;
    Button go;
    String image_url_combination = "";
    String image_string;
    List<Uri> image_uris;
    public StorageReference storageReference;
    int k = 0;
    int filledTill = 0;
    AskingRuntimePermissions askingRuntimePermissions;
    Uri photoUri;
    FirebaseUser mUser;
    List<String> liked;
    List<String> likedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference(mUser.getUid() + "/");

        image_uris = new ArrayList<>();
        liked = new ArrayList<>();
        likedBy = new ArrayList<>();

        imageView1 = (ImageView) findViewById(R.id.imageview1);
        imageView2 = (ImageView) findViewById(R.id.imageview2);
        imageView3 = (ImageView) findViewById(R.id.imageview3);
        imageView4 = (ImageView) findViewById(R.id.imageview4);
        imageView5 = (ImageView) findViewById(R.id.imageview5);
        imageView6 = (ImageView) findViewById(R.id.imageview6);
        imageView7 = (ImageView) findViewById(R.id.imageview7);
        imageView8 = (ImageView) findViewById(R.id.imageview8);
        imageView9 = (ImageView) findViewById(R.id.imageview9);

        go = (Button) findViewById(R.id.nextfive);

        //image_url_combination = getIntent().getStringExtra("facebookdp");
       //Picasso.get().load(Uri.parse(getIntent().getStringExtra("facebookdp"))).into(imageView1);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGalleryOpener();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGalleryOpener();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGalleryOpener();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGalleryOpener();
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGalleryOpener();
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGalleryOpener();
            }
        });

        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGalleryOpener();
            }
        });

        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGalleryOpener();
            }
        });

        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGalleryOpener();
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload to the storage
                uploadFile();
                //get links seperated with @

                //save in database number of images and image links
            }
        });

    }

    public void uploadFile() {
        if(filledTill > 0){
            if(k < filledTill) {
                showloadingdialog();
                final StorageReference fileReference = storageReference.child("pictures/" + mUser.getUid() + k + "." + getFileExtension(image_uris.get(k)));
                fileReference.putFile(image_uris.get(k))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        image_string = uri.toString();
                                        image_url_combination += image_string + "@";

                                        k++;
                                        uploadFile();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Imagedialog.dismiss();
                                        Toast.makeText(PhotoActivity.this, "Links can't be generated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Imagedialog.dismiss();
                                Toast.makeText(PhotoActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }else{
                fillDatabase();
            }
        }else{
            Toast.makeText(this, "No Image selected.", Toast.LENGTH_SHORT).show();
        }

    }

    private void fillDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        User user = new User(getIntent().getStringExtra("name"), getIntent().getStringExtra("age"), filledTill, getIntent().getStringExtra("gender"), getIntent().getDoubleExtra("longitude",0), getIntent().getDoubleExtra("latitude",0), getIntent().getStringExtra("address"), getIntent().getStringExtra("facebookdp"), getIntent().getStringExtra("id"), image_url_combination.substring(0,image_url_combination.length()-1), getIntent().getStringExtra("matchwith"), getIntent().getStringExtra("lookingfor"), getIntent().getStringExtra("userId"), liked, likedBy, "Hey there, I am using Fond.", "done");

        db.collection("Users").document(getIntent().getStringExtra("userId"))
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Imagedialog.dismiss();
                        updateUi();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Imagedialog.dismiss();
                    }
                });
    }

    private void updateUi() {
        Intent intent = new Intent(PhotoActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void callGalleryOpener() {
        //open gallery take photo, call imagefillchecker
        askingRuntimePermissions = new AskingRuntimePermissions(this);
        if (askingRuntimePermissions.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) || askingRuntimePermissions.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            askingRuntimePermissions.askPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
         switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    photoUri = imageReturnedIntent.getData();
                    image_uris.add(photoUri);
                    imageFillChecker(photoUri);
                }
                break;
        }
    }

    private void imageFillChecker(Uri uri) {
        switch (filledTill) {
            case 0: {
                filledTill = 1;
                imageView1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(uri).into(imageView1);
            }
            break;

            case 1: {
                filledTill = 2;
                imageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(uri).into(imageView2);
            }
            break;

            case 2: {
                filledTill = 3;
                imageView3.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(uri).into(imageView3);
            }
            break;

            case 3: {
                filledTill = 4;
                imageView4.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(uri).into(imageView4);
            }
            break;

            case 4: {
                filledTill = 5;
                imageView5.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(uri).into(imageView5);
            }
            break;

            case 5: {
                filledTill = 6;
                imageView6.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(uri).into(imageView6);
            }
            break;

            case 6: {
                filledTill = 7;
                imageView7.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(uri).into(imageView7);
            }
            break;

            case 7: {
                filledTill = 8;
                imageView8.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(uri).into(imageView8);

            }
            break;

            case 8: {
                filledTill = 9;
                imageView9.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(uri).into(imageView9);
            }
            break;

            case 9: {
                filledTill = 9;
                imageView9.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView9.setImageURI(uri);
            }
            break;
            default: {
                Toast.makeText(this, "Can't upload anymore.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {
                case 1: {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                        openGallery();

                    } else {
                        Toast.makeText(this, "Permission(s) denied", Toast.LENGTH_SHORT).show();
                        askingRuntimePermissions.askPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
                    }
                    break;
                }
            }
        }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void showloadingdialog() {
        Imagedialog = new Dialog(PhotoActivity.this, R.style.DialogTheme);
        Imagedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Imagedialog.setCancelable(false);
        Imagedialog.setContentView(R.layout.photo_uploader);
        Imagedialog.getWindow().setGravity(Gravity.CENTER);
        Imagedialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        Imagedialog.show();
    }

}
