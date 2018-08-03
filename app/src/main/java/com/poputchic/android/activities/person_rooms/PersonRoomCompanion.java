package com.poputchic.android.activities.person_rooms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.poputchic.android.FontsDriver;
import com.poputchic.android.R;
import com.poputchic.android.activities.person_rooms.my_travels.MyFinishesTravels;
import com.poputchic.android.bottom_toolbar.BottomToolbarController;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Review;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonRoomCompanion extends Activity {

    private Companion companion;
    private ProgressBar private_room_progress_bar_c;
    private ImageView pr_iv_my_travels_c, pr_iv_edit_profile_c;
    private TextView pr_tv_name_c, pr_tv_years_c, pr_tv_email_c, pr_tv_number_phone_c,pr_tv_about_c;
    private CircleImageView pr_CIV_image_companion;
    private ListView pr_lv_reviews_c;
    private ArrayList<Review> listReview;
    public static final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_room_companion);

        init();
        changeFonts(); // изменяем шрифты вьюх
    }

    private void changeFonts() {
        FontsDriver.changeFontToComfort(this,(TextView)findViewById(R.id.activity_person_room_companion_label));
        FontsDriver.changeFontToComfort(this,pr_tv_name_c);
        FontsDriver.changeFontToComfort(this,pr_tv_years_c);
        FontsDriver.changeFontToComfort(this,pr_tv_email_c);
        FontsDriver.changeFontToComfort(this,pr_tv_number_phone_c);
        FontsDriver.changeFontToComfort(this,pr_tv_about_c);
    }

    public void click(View view) {
        BottomToolbarController controller = new BottomToolbarController(this);
        switch (view.getId()) {
            case R.id.b_menu_1:
                //controller.myProfile(null,companion);
                break;
            case R.id.b_menu_2:
                finish();
                //controller.exit();
                break;
            case R.id.b_menu_3:
                controller.addClick(null,companion);
                break;
            case R.id.b_menu_4:
                controller.imCompanoin(null,companion);
                break;
            case R.id.b_menu_5:
                // ?
                controller.usersList(null,companion);
                break;
        }
    }

    private void init() {

        private_room_progress_bar_c = (ProgressBar) findViewById(R.id.private_room_progress_bar_c);

        pr_iv_my_travels_c = (ImageView) findViewById(R.id.pr_iv_my_travels_c);
        pr_iv_edit_profile_c = (ImageView) findViewById(R.id.pr_iv_edit_profile_c);

        pr_tv_name_c = (TextView) findViewById(R.id.pr_tv_name_c);
        pr_tv_years_c = (TextView) findViewById(R.id.pr_tv_years_c);
        pr_tv_email_c = (TextView) findViewById(R.id.pr_tv_email_c);
        pr_tv_number_phone_c = (TextView) findViewById(R.id.pr_tv_number_phone_c);
        pr_tv_about_c = (TextView) findViewById(R.id.pr_tv_about_c);

        pr_CIV_image_companion = (CircleImageView) findViewById(R.id.pr_CIV_image_companion);

        pr_lv_reviews_c = (ListView) findViewById(R.id.pr_lv_reviews_c);

        listReview = new ArrayList<>();

        clicker();
        takeCompanion();
    }

    private void clicker() {
        pr_iv_edit_profile_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });
        pr_CIV_image_companion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndOpenFilePicker();
            }
        });
        pr_iv_my_travels_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonRoomCompanion.this, MyFinishesTravels.class);
                intent.putExtra("companion",companion);
                startActivity(intent);
            }
        });
    }

    private void checkPermissionsAndOpenFilePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                pr_CIV_image_companion.setImageBitmap(bitmap);
                uploadPhotoToFirebase(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadPhotoToFirebase(Bitmap bitmap) {
        FirebaseStorage storage;
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Загрузка...");
            progressDialog.show();

            StorageReference ref = storageReference.child("users_images/"+ UUID.randomUUID().toString());

            //Log.d(MainActivity.LOG_TAG,"path image TEST = " + filePath);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            //Log.d(MainActivity.LOG_TAG,"path image = " + taskSnapshot.getDownloadUrl());
                            Toast.makeText(PersonRoomCompanion.this, "Загружено!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference().child("users")
                                    .child("companion").child(companion.getDate_create()+"").child("image_path")
                                    .setValue(taskSnapshot.getDownloadUrl()+"");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PersonRoomCompanion.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Загружено "+(int)progress+"%");
                        }
                    });
        }
    }

    private void editProfile() {
        Intent intent = new Intent(PersonRoomCompanion.this, EditCompanionProfile.class);
        intent.putExtra("companion", companion);
        startActivity(intent);
    }

    private void stack() {
        completeViews();
    }

    private void completeViews() {
        if (companion != null) {

            if (companion.getImage_path() != null) {
                Picasso.with(this).load(companion.getImage_path()).into(pr_CIV_image_companion, new Callback() {
                    @Override
                    public void onSuccess() {
                        private_room_progress_bar_c.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
            } else {
                Picasso.with(this).load("http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png").into(pr_CIV_image_companion);
            }
            if (companion.getName() != null) {
                pr_tv_name_c.setText(companion.getName());
            }
            if (companion.getYear() != 0) {
                pr_tv_years_c.setText(companion.getYear() + " лет");
            }
            if (companion.getEmail() != null) {
                pr_tv_email_c.setText(companion.getEmail());
            }
            if (companion.getPhone() != null) {
                pr_tv_number_phone_c.setText(companion.getPhone());
            }
            if (companion.getAbout()!=null){
                pr_tv_about_c.setText(companion.getAbout()+"");
            }
            // add list review
        }
    }

    private void takeCompanion() {
        companion = (Companion) getIntent().getSerializableExtra("companion");

        FirebaseDatabase.getInstance().getReference().child("users").child("companion")
                .child(companion.getDate_create()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                companion = (Companion) dataSnapshot.getValue(Companion.class);
                stack();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
