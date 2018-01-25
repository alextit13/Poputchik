package com.poputchic.android.activities.person_rooms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.poputchic.android.R;
import com.poputchic.android.classes.classes.Driver;
import com.poputchic.android.classes.classes.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonRoomDriver extends AppCompatActivity {

    private Driver driver;

    private ImageView pr_iv_my_travels
            ,pr_iv_edit_profile;
    private RatingBar pr_rb_rating_bar;
    private TextView pr_tv_rating,pr_tv_name,pr_tv_years,pr_tv_email,pr_tv_number_phone,pr_tv_car;
    private CircleImageView pr_CIV_image_driver;
    private ListView pr_lv_reviews;

    private ArrayList<Review>listReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_room_driver);

        init();
    }

    private void init() {
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
        stack();
    }

    private void clicker() {
        pr_iv_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });
    }

    private void editProfile() {
        Intent intent = new Intent(PersonRoomDriver.this,EditDriverProfile.class);
        intent.putExtra("driver",driver);
        startActivity(intent);
    }

    private void stack() {
        completeViews();
    }

    private void completeViews() {
        if (driver!=null){
            if (driver.getRating()!=null){pr_rb_rating_bar.setRating(Float.parseFloat(driver.getRating()));pr_tv_rating.setText(driver.getRating());}
            if (driver.getImage_path()!=null){Picasso.with(this).load(driver.getImage_path()).into(pr_CIV_image_driver);}else{Picasso.with(this).load("http://www.clker.com/cliparts/B/R/Y/m/P/e/blank-profile-hi.png").into(pr_CIV_image_driver);}
            if (driver.getName()!=null){pr_tv_name.setText(driver.getName());}
            if (driver.getYear()!=0){pr_tv_years.setText(driver.getYear()+" лет");}
            if (driver.getEmail()!=null){pr_tv_email.setText(driver.getEmail());}
            if (driver.getNumberPhone()!=null){pr_tv_number_phone.setText(driver.getNumberPhone());}
            if (driver.getName_car()!=null&&driver.getYear_car()!=null){pr_tv_car.setText(driver.getName_car()+" " + driver.getYear_car());}
            // add list review
        }
    }

    private void takeDriver() {
        driver = (Driver) getIntent().getSerializableExtra("driver");
    }
}
