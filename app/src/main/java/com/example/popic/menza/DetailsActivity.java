package com.example.popic.menza;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.toolbarDetails)
    Toolbar toolbarDetails;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.textView_ratinDetails)
    TextView textView_ratingDetails;
    SupportMapFragment mapFragment;
    @BindView(R.id.textView_restaurantNameDetails)
    TextView textView_restaurantName;
    @BindView(R.id.button_vote)
    Button button_vote;
    @BindView(R.id.button_submit)
    Button button_submit;

    String name, cityName;
    Double longitude, latitude, noVotes, votesSum, userLongitude, userLatitude;
    Intent intent;
    Bundle extras;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setSupportActionBar(toolbarDetails);
        ButterKnife.bind(this);


        intent = getIntent();
        extras = intent.getExtras();

        auth = FirebaseAuth.getInstance();

        name = extras.getString(Constants.restaurantName);
        longitude = Double.parseDouble(extras.getString(Constants.longitude));
        latitude = Double.parseDouble(extras.getString(Constants.latitude));
        noVotes = Double.parseDouble(extras.getString(Constants.noVotes));
        votesSum = Double.parseDouble(extras.getString(Constants.votesSum));
        cityName = extras.getString(Constants.cityName);


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        setData();

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        String name = extras.getString(Constants.restaurantName);
        double longitude = Double.parseDouble(extras.getString(Constants.longitude));
        double latitude = Double.parseDouble(extras.getString(Constants.latitude));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(longitude, latitude))
                .title(name).icon(BitmapDescriptorFactory.defaultMarker()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(longitude, latitude), 13));

    }

    private void setData() {

        textView_restaurantName.setText(name);
        double rating = votesSum / noVotes;
        ratingBar.setRating((float) rating);

        NumberFormat numberFormat = new DecimalFormat("#0.00");
        String stringRating = numberFormat.format(rating);
        textView_ratingDetails.setText(stringRating);


    }

    @OnClick(R.id.button_vote)
    public void userRating() {

        ratingBar.setIsIndicator(false);
        ratingBar.setStepSize(1);
        button_vote.setVisibility(View.GONE);
        button_submit.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.button_submit)
    public void submitVote() {

        ratingBar.setIsIndicator(true);
        ratingBar.setStepSize((float) 0.01);
        button_submit.setVisibility(View.GONE);
        button_vote.setVisibility(View.VISIBLE);

        double numStars = ratingBar.getRating();
        votesSum = votesSum + numStars;
        noVotes++;
        double rating = votesSum / noVotes;
        ratingBar.setRating((float) rating);

        NumberFormat numberFormat = new DecimalFormat("#0.00");
        String stringRating = numberFormat.format(rating);
        textView_ratingDetails.setText(stringRating);

        addToFirebase(votesSum, noVotes);

    }

    private void addToFirebase(double votesSum, double noVotes) {

        DatabaseReference newEntry = FirebaseDatabase.getInstance().getReference().child("cities").child(cityName).child(name);

        newEntry.child("noVotes").setValue(String.valueOf(noVotes));
        newEntry.child("votesSum").setValue(String.valueOf(votesSum));

    }

    @OnClick(R.id.button_logoutDetails)
    public void logoutDetails(){
        auth.signOut();
        startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
        finish();
    }
}
