package com.poputchic.android.reg_and_sign;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.poputchic.android.R;
import com.poputchic.android.activities.MainListActivity;
import com.poputchic.android.classes.classes.Companion;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Review;
import com.poputchic.android.classes.classes.Travel;

import java.util.ArrayList;
import java.util.Date;

import me.iwf.photopicker.PhotoPicker;

import static com.poputchic.android.classes.VARIABLES_CLASS.LOG_TAG;
import static com.poputchic.android.classes.VARIABLES_CLASS.PERMISSIONS_REQUEST_CODE;

public class Registration extends Activity {

    private FrameLayout a_fl_container;
    private EditText    a_et_email,a_et_password,a_et_password_confirm,a_et_model_auto,a_et_number_of_phone;
    private RadioButton a_cb_driver,a_cb_companion;
    private Button      a_b_chose_photo,a_b_registration;
    private ImageView   a_iv_back,a_iv_icon_photo;
    private String      image_path;
    private Driver      driver;
    private Companion   companion;
    private int ID =    0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    private void init() {
        a_fl_container = (FrameLayout) findViewById(R.id.a_fl_container);

        a_et_email = (EditText)findViewById(R.id.a_et_email);
        a_et_password = (EditText)findViewById(R.id.a_et_password);
        a_et_password_confirm = (EditText)findViewById(R.id.a_et_password_confirm);
        a_et_model_auto = (EditText)findViewById(R.id.a_et_model_auto);
        a_et_number_of_phone = (EditText)findViewById(R.id.a_et_number_of_phone);

        a_cb_driver = (RadioButton)findViewById(R.id.a_cb_driver);
        a_cb_companion = (RadioButton)findViewById(R.id.a_cb_companion);

        a_b_chose_photo = (Button)findViewById(R.id.a_b_chose_photo);
        a_b_registration = (Button)findViewById(R.id.a_b_registration);

        a_iv_back = (ImageView)findViewById(R.id.a_iv_back);
        a_iv_icon_photo = (ImageView)findViewById(R.id.a_iv_icon_photo);

        ((ProgressBar) findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);
    }

    public void click_buttons(View view) {
        switch (view.getId()){
            case R.id.a_iv_back:
                // back
                closeApplication();
                break;
            case R.id.a_b_registration:
                //registration
                registration();
                break;
            case R.id.a_b_chose_photo:
                //chose photo
                chosePhoto();
                break;
            case R.id.a_cb_driver:
                // rb_driver
                clickRB_driver();
                break;
            case R.id.a_cb_companion:
                // rb_companion
                clickRB_companion();
                break;
            default:
                break;
        }
    }

    private void clickRB_companion() {
        a_et_model_auto.setVisibility(View.INVISIBLE);
    }

    private void clickRB_driver() {
        a_et_model_auto.setVisibility(View.VISIBLE);
    }

    private void chosePhoto() {
        ID = a_iv_icon_photo.getId();
        checkPermissionsAndOpenFilePicker();
    }

    private void checkPermissionsAndOpenFilePicker() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showError();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {

        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    private void showError() {
        Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFilePicker();
                } else {
                    showError();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (data != null){
            ArrayList<String> photos =
                    data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            //filePaths.add(photos.get(0));

            image_path = "";
            image_path = photos.get(0);
            if (ID!=0){
                Bitmap bitmap = BitmapFactory.decodeFile(image_path);
                /*bitmap = Bitmap.createScaledBitmap(bitmap,photo_docs_image_license.getWidth(),photo_docs_image_license.getHeight(),true);
                ((ImageView)findViewById(ID)).setImageBitmap(bitmap);*/
                bitmap = Bitmap.createScaledBitmap(bitmap,a_iv_icon_photo.getWidth(),a_iv_icon_photo.getHeight(),true);
                ((ImageView)findViewById(ID)).setImageBitmap(bitmap);
                Log.d(LOG_TAG,"photos = " + bitmap.toString());
            }
        }
    }

    private void registration() {
        hideContainer();
        if (checkCompleteAllFields()){ // if all fields is completed
            if (a_cb_driver.isChecked()){
                // create_driver
                driver = createDriver();
                pushDriverToFirebase(driver);
            }else if (a_cb_companion.isChecked()){
                // create companion
                companion = createCompanion();
                pushCompanionToFirebase(companion);
            }
        }
    }

    private void hideContainer() {
        a_fl_container.setAlpha(.3f);
    }

    private void pushCompanionToFirebase(final Companion c) {
        ((ProgressBar) findViewById(R.id.a_pb)).setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("companions").child(c.getDate_of_create()+"")
                .setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Registration.this, "вы успешно зарегистрированы!", Toast.LENGTH_SHORT).show();
                ((ProgressBar) findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Registration.this, MainListActivity.class);
                intent.putExtra("driver_or_companion","companion");
                intent.putExtra("companion",c);
                startActivity(intent);
            }
        });
    }

    private Companion createCompanion() {
        companion = new Companion(a_et_email.getText().toString(),"name_companion",new Date().getTime(),image_path,new ArrayList<Review>(),
                "0","0","about_me",a_et_number_of_phone.getText().toString(),"lon","lat");
        return companion;
    }

    private void pushDriverToFirebase(final Driver d) {
        ((ProgressBar) findViewById(R.id.a_pb)).setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("users")
                .child("drivers").child(d.getDate_of_create()+"")
                .setValue(d).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ((ProgressBar) findViewById(R.id.a_pb)).setVisibility(View.INVISIBLE);
                Toast.makeText(Registration.this, "вы успешно зарегистрированы!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Registration.this, MainListActivity.class);
                intent.putExtra("driver_or_companion","driver");
                intent.putExtra("driver",d);
                startActivity(intent);
            }
        });
    }

    private Driver createDriver() {
        driver = new Driver(a_et_email.getText().toString(),"name_driver",new Date().getTime(),image_path,new ArrayList<Review>(),
                "0","0","",a_et_number_of_phone.getText().toString(),"","",
                new ArrayList<Travel>(),new ArrayList<Travel>());
        return driver;
    }

    private boolean checkCompleteAllFields() {
        boolean check = false;
        image_path = checkEmptyImage(image_path);
        if (!a_et_password.getText().toString().equals("")&&!a_et_password_confirm.getText().toString().equals("")&&
                a_et_password.getText().toString().equals(a_et_password_confirm.getText().toString())){
            if (!a_et_email.getText().toString().equals("")&&!a_et_password.getText().toString().equals("")&&!a_et_password_confirm.getText().toString().equals("")&&
                    !a_et_model_auto.getText().toString().equals("")&&!a_et_number_of_phone.getText().toString().equals("")){
                if (!image_path.equals("")){
                    if (a_cb_driver.isChecked()||a_cb_companion.isChecked()){
                        check = true;
                    }
                }
            }
        }
        if (!check){
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    private String checkEmptyImage(String IP) {
        if (IP==null||IP.equals("")){
            IP="http://www.sitechecker.eu/img/not-available.png";
        }
        return IP;
    }

    private void closeApplication() {
        this.finish();
    }
}
