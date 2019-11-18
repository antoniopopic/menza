package com.example.popic.menza;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.textView_helloUser)
    TextView textView_helloUser;
    @BindView(R.id.recyclerView_main)
    RecyclerView recyclerView_main;
    @BindView(R.id.toolbarMain)
    Toolbar toolbarMain;
    @BindView(R.id.bottom_sheet)
    View bottom_sheet;
    @BindView(R.id.recyclerView_restaurants)
    RecyclerView recyclerView_restaurants;

    private BottomSheetBehavior bottomSheetBehavior;


    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Query query, query2;

    MainAdapter adapter;
    MainAdapterClickListener mainAdapterClickListener = new MainAdapterClickListener() {
        @Override
        public void onClick(City city) {
            showRestaurants(city);
        }
    };

    SheetAdapter sheetAdapter;
    SheetAdapterClickListener sheetAdapterClickListener = new SheetAdapterClickListener() {
        @Override
        public void onClick(Restaurant restaurant) {
            openRestaurant(restaurant);
        }
    };

    private List<City> cityList;
    private List<Restaurant> restaurantList;
    private City city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        setFirebase();
        setMainRecycler();
        setSheetRecycler();
        getCities();


        setSupportActionBar(toolbarMain);
        setToolbar();
    }


    private void setToolbar() {
        currentUser = auth.getCurrentUser();
        String userID = currentUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        query = reference.orderByChild(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.getChildren();
                    textView_helloUser.setText("Hello " + dataSnapshot.getValue(User.class).getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.button_logout)
    public void logoutUser() {

        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }


    private void setFirebase() {
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("cities");
        reference.keepSynced(true);
    }

    private void getCities() {

        query = reference;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fetchData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void setMainRecycler() {

        cityList = new ArrayList<>();
        adapter = new MainAdapter(mainAdapterClickListener, cityList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_main.setLayoutManager(linearLayoutManager);
        recyclerView_main.setItemAnimator(new DefaultItemAnimator());
        recyclerView_main.setAdapter(adapter);

    }

    private void setSheetRecycler() {
        restaurantList = new ArrayList<>();

        sheetAdapter = new SheetAdapter(sheetAdapterClickListener, restaurantList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_restaurants.setLayoutManager(linearLayoutManager);
        recyclerView_restaurants.setItemAnimator(new DefaultItemAnimator());
        recyclerView_restaurants.setAdapter(sheetAdapter);

    }


    private void fetchData(DataSnapshot dataSnapshot) {

        if (dataSnapshot != null) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                city = new City();
                String cityName = ds.getKey();
                city.setCityName(cityName);
                cityList.add(city);
            }
            adapter = new MainAdapter(mainAdapterClickListener, cityList);
            recyclerView_main.setAdapter(adapter);
        }


    }


    private void showRestaurants(City city) {

        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottom_sheet.setVisibility(View.VISIBLE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetBehavior.setHideable(true);
            city.setOpened(true);
            setData(city.getCityName());
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            if (!city.isOpened()) {
                bottom_sheet.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetBehavior.setHideable(true);
                city.setOpened(true);
                setData(city.getCityName());
                city.setOpened(false);
            } else {
                city.setOpened(false);
            }
        } else {
            bottom_sheet.setVisibility(View.GONE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            city.setOpened(false);
        }


    }

    public void setData(String cityName) {


        final List<Restaurant> restaurantList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("cities").child(cityName);
        query2 = reference;
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Restaurant newRestaurant = new Restaurant();
                        String restaurantName = ds.child("name").getValue(String.class);
                        String votesSum = ds.child("votesSum").getValue(String.class);
                        String noVotes = ds.child("noVotes").getValue(String.class);
                        String cityName = ds.child("cityName").getValue(String.class);
                        String longitude = ds.child("longitude").getValue(String.class);
                        String latitude = ds.child("latitude").getValue(String.class);
                        newRestaurant.setName(restaurantName);
                        newRestaurant.setVotesSum(votesSum);
                        newRestaurant.setNoVotes(noVotes);
                        newRestaurant.setCityName(cityName);
                        newRestaurant.setLatitude(latitude);
                        newRestaurant.setLongitude(longitude);
                        restaurantList.add(newRestaurant);
                    }
                    sheetAdapter = new SheetAdapter(sheetAdapterClickListener, restaurantList);
                    recyclerView_restaurants.setAdapter(sheetAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openRestaurant(Restaurant restaurant) {

        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.restaurantName, restaurant.getName());
        bundle.putString(Constants.cityName, restaurant.getCityName());
        bundle.putString(Constants.latitude, restaurant.getLatitude());
        bundle.putString(Constants.longitude, restaurant.getLongitude());
        bundle.putString(Constants.noVotes, restaurant.getNoVotes());
        bundle.putString(Constants.votesSum, restaurant.getVotesSum());
        intent.putExtras(bundle);
        startActivity(intent);

    }


}
