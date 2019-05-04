package com.krashunt.krashunt2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LocationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
  private String Location,userID;
    private FirebaseDatabase mFirebaseDatabase;
    private ImageView tick1,tick2; private Button inbutton,hkbutton,subbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        tick1 = (ImageView) findViewById(R.id.tick1);
        tick2 = (ImageView) findViewById(R.id.tick2);

        inbutton = (Button) findViewById(R.id.india);
        hkbutton = (Button) findViewById(R.id.hongkong);
        subbutton = (Button) findViewById(R.id.btnSubmit);
    }
 public void hk(View v){

  subbutton.setAlpha(255);
     subbutton.setEnabled(true);
     tick2.setVisibility(GONE);

        tick1.setVisibility(VISIBLE);
        Location = "Hong Kong";

     SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
     SharedPreferences.Editor editor = preferences.edit();
     editor.putString("Location",Location);
     editor.apply();
    // inbutton.setEnabled(false);

 }

    public void in(View v){

        subbutton.setAlpha(255);
        subbutton.setEnabled(true);
        tick1.setVisibility(GONE);
        tick2.setVisibility(VISIBLE);
        Location = "India";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Location",Location);
        editor.apply();
      //  hkbutton.setEnabled(false);
    }
    public void submit(View v){

        //Log.d("Location",Location);
        myRef.child("User").child(userID).child("location").setValue(Location);


        Intent mIntent = getIntent();
        String check = mIntent.getStringExtra("first");
        if(check!=null){

            Intent intent = new Intent(LocationActivity.this,WelcomeActivity.class);
            intent.putExtra("first","first");
            startActivity(intent);

        }
        else{
            onBackPressed();}




    }
    public void backloc(View v){

        onBackPressed();
    }



}
