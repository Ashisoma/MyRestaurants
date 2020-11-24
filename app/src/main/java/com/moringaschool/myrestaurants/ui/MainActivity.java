package com.moringaschool.myrestaurants.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myrestaurants.Constants;
import com.moringaschool.myrestaurants.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    private SharedPreferences mSharedPreferences;
//    private SharedPreferences.Editor mEditor;


    public static String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @BindView(R.id.locationEditText) EditText mLocationEditText;
    @BindView(R.id.appNameTextView) TextView mAppNameTextView;

    private DatabaseReference mSearchedLocationReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mSearchedLocationReference = FirebaseDatabase.getInstance()
                .getReference().child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION);// pinpoint the location name

        mSearchedLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot locationSnapshot : snapshot.getChildren()){
                    String location = locationSnapshot.getValue().toString();
                    Log.d("Locations updated","location" + location); //logging
                }
            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        Typeface ostrichFont = Typeface.createFromAsset(getAssets(), "fonts/ostrich-regular.ttf");
//        mAppNameTextView.setTypeface(ostrichFont);

    //        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    //        mEditor = mSharedPreferences.edit();

        mFindRestaurantsButton.setOnClickListener(this);

    }
    @Override
    public void onClick(View v){
                if (v == mFindRestaurantsButton){
                    String location = mLocationEditText.getText().toString();
                    saveLocationToFirebase(location);
                    Intent intent = new Intent(MainActivity.this, RestaurantsListActivity.class);
                    intent.putExtra("location", location);
                    startActivity(intent);
            }
    }

    private void saveLocationToFirebase(String location){
        mSearchedLocationReference.push().setValue(location);
    }

//    private void addToSharedPreferences(String location){
//        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
//    }
}