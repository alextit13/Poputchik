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
import android.widget.RatingBar;
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
import com.poputchic.android.models.fonts.FontsConteroller;
import com.poputchic.android.R;
import com.poputchic.android.activities.person_rooms.my_travels.MyTravels;
import com.poputchic.android.bottom_toolbar.BottomToolbarController;
import com.poputchic.android.models.driver.Driver;
import com.poputchic.android.models.Review;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonRoomDriver extends Activity {

    private Driver driver;

    private ProgressBar private_room_progress_bar;

    private ImageView pr_iv_my_travels, pr_iv_edit_profile;
    private RatingBar pr_rb_rating_bar;
    private TextView pr_tv_rating, pr_tv_name, pr_tv_years, pr_tv_email, pr_tv_number_phone, pr_tv_car;
    private CircleImageView pr_CIV_image_driver;
    private ListView pr_lv_reviews;

    private ArrayList<Review> listReview;
    public static final int PERMISSIONS_REQUEST_CODE = 0;
    private String changeImagePath = "";
    public static final int PICK_IMAGE_REQUEST = 71;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_room_driver);

        init();
        changeFonts(); // изменяем шрифты вьюх
    }

    private void changeFonts() {
        /*FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.activity_person_room_driver));
        FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.pr_tv_rating));
        FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.pr_tv_name));
        FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.pr_tv_years));
        FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.pr_tv_email));
        FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.pr_tv_number_phone));
        FontsConteroller.changeFontToComfort(this,(TextView)findViewById(R.id.pr_tv_car));*/
    }

    public void click(View view) {
        BottomToolbarController controller = new BottomToolbarController(this);
        switch (view.getId()) {
            case R.id.b_menu_1:
                //controller.myProfile(driver,null);
                break;
            case R.id.b_menu_2:
                //controller.exit();
                finish();
                break;
            case R.id.b_menu_3:
                controller.addClick(driver,null);
                break;
            case R.id.b_menu_4:
                controller.imCompanoin(driver,null);
                break;
            case R.id.b_menu_5:
                // ?
                controller.usersList(driver,null);
                break;
        }
    }

    private void init() {

        private_room_progress_bar = (ProgressBar) findViewById(R.id.private_room_progress_bar);
        pr_iv_my_travels = (ImageView) findViewById(R.id.pr_iv_my_travels);
        pr_iv_edit_profile = (ImageView) findViewById(R.id.pr_iv_edit_profile);

        pr_rb_rating_bar = (RatingBar) findViewById(R.id.pr_rb_rating_bar);

        pr_tv_rating = (TextView) findViewById(R.id.pr_tv_rating);
        pr_tv_name = (TextView) findViewById(R.id.pr_tv_name);
        pr_tv_years = (TextView) findViewById(R.id.pr_tv_years);
        pr_tv_email = (TextView) findViewById(R.id.pr_tv_email);
        pr_tv_number_phone = (TextView) findViewById(R.id.pr_tv_number_phone);
        pr_tv_car = (TextView) findViewById(R.id.pr_tv_car);

        pr_CIV_image_driver = (CircleImageView) findViewById(R.id.pr_CIV_image_driver);

        pr_lv_reviews = (ListView) findViewById(R.id.pr_lv_reviews);

        listReview = new ArrayList<>();

        clicker();
        takeDriver();
    }

    private void clicker() {
        pr_iv_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });
        pr_CIV_image_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndOpenFilePicker();
            }
        });
        pr_iv_my_travels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonRoomDriver.this, MyTravels.class);
                intent.putExtra("driver",driver);
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
                pr_CIV_image_driver.setImageBitmap(bitmap);
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
                            Toast.makeText(PersonRoomDriver.this, "Загружено!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference().child("users")
                                    .child("drivers").child(driver.getDate_create()+"").child("image_path")
                                    .setValue(taskSnapshot.getDownloadUrl()+"");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PersonRoomDriver.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(PersonRoomDriver.this, EditDriverProfile.class);
        intent.putExtra("driver", driver);
        startActivity(intent);
    }

    private void stack() {
        completeViews();
    }

    private void completeViews() {
        if (driver != null) {
            if (driver.getRating() != null) {
                pr_rb_rating_bar.setRating(Float.parseFloat(driver.getRating()));
                pr_tv_rating.setText(driver.getRating());
            }
            if (driver.getImage_path() != null) {
                Picasso.with(this).load(driver.getImage_path()).into(pr_CIV_image_driver, new Callback() {
                    @Override
                    public void onSuccess() {
                        private_room_progress_bar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
            } else {
                Picasso.with(this).load("http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png").into(pr_CIV_image_driver);
            }
            if (driver.getName() != null) {
                pr_tv_name.setText(driver.getName());
            }
            if (driver.getYear() != 0) {
                pr_tv_years.setText(driver.getYear() + " лет");
            }
            if (driver.getEmail() != null) {
                pr_tv_email.setText(driver.getEmail());
            }
            if (driver.getNumberPhone() != null) {
                pr_tv_number_phone.setText(driver.getNumberPhone());
            }
            if (driver.getName_car() != null && driver.getYear_car() != null) {
                pr_tv_car.setText(driver.getName_car() + " " + driver.getYear_car());
            }
            // add list review
        }
    }

    private void takeDriver() {
        driver = (Driver) getIntent().getSerializableExtra("driver");

        FirebaseDatabase.getInstance().getReference().child("users").child("drivers")
                .child(driver.getDate_create()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driver = (Driver) dataSnapshot.getValue(Driver.class);
                stack();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
