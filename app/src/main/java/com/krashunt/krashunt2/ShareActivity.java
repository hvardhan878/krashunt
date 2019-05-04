package com.krashunt.krashunt2;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShareActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private Receiver mInternetStatusNotifier;

    private RewardedVideoAd mRewardedVideoAd;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef,Ref,bonus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInternetStatusNotifier = new Receiver();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        Ref = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                final String coinsdata= dataSnapshot.getValue().toString();

                TextView coins = (TextView) findViewById(R.id.coins);

                coins.setText(coinsdata);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        MobileAds.initialize(this, "ca-app-pub-1407187642190765~7697478772");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();



    }

    @Override
    protected void onResume() {

        registerReceiver(mInternetStatusNotifier, new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE"));
        super.onResume();
        loadRewardedVideoAd();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mInternetStatusNotifier);
        super.onPause();
    }
    private void loadRewardedVideoAd() {


        mRewardedVideoAd.loadAd("ca-app-pub-1407187642190765/6134127424", new AdRequest.Builder().build());
       // AdRequest.Builder.addTestDevice("D07F24EF278F01FA32754B7434508259")
    }
    private void showRewardedVideo() {
        //mShowVideoButton.setVisibility(View.INVISIBLE);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }
    public void backhome(View v){

        onBackPressed();

    }

  /*public void shareapp(View v){

                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        String shareBodyText = "Go Check Our App Krashunt";
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                        startActivity(Intent.createChooser(intent, "Choose sharing method"));




    }*/
    public void watchad(View v){


        showRewardedVideo();


    }
    public void invite(View v){

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        String url1 = "https://goo.gl/frNFVX";
        String shareBodyText = "Go Check Our App Krashunt "+url1;
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(intent, "Choose sharing method"));


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
       String userID = user.getUid();
       bonus = mFirebaseDatabase.getReference().child("User").child(userID);



     bonus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
              if(dataSnapshot.child("Bonus1").getValue().toString().equals("No")||dataSnapshot.child("Bonus1").getValue()==null){



              String coinsdata= dataSnapshot.child("coins").getValue().toString();

                  int coinsdataold = Integer.parseInt(coinsdata);
                  int coinsdatanew = coinsdataold +100;
                  String coinsdatanewString = String.valueOf(coinsdatanew);

                  bonus.child("coins").setValue(coinsdatanewString);


                  bonus.child("Bonus1").setValue("Yes");

              }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void follow(View v){




        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        bonus = mFirebaseDatabase.getReference().child("User").child(userID);



        bonus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                if(dataSnapshot.child("Bonus3").getValue().toString().equals("No")||dataSnapshot.child("Bonus3").getValue()==null){



                    String coinsdata= dataSnapshot.child("coins").getValue().toString();

                    int coinsdataold = Integer.parseInt(coinsdata);
                    int coinsdatanew = coinsdataold +100;
                    String coinsdatanewString = String.valueOf(coinsdatanew);

                    bonus.child("coins").setValue(coinsdatanewString);


                    bonus.child("Bonus3").setValue("Yes");

                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String url = "https://www.instagram.com/krashuntlimited/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);


    }

    public void like(View v){




        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        bonus = mFirebaseDatabase.getReference().child("User").child(userID);



        bonus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                if(dataSnapshot.child("Bonus2").getValue().toString().equals("No")||dataSnapshot.child("Bonus2").getValue()==null){



                    String coinsdata= dataSnapshot.child("coins").getValue().toString();

                    int coinsdataold = Integer.parseInt(coinsdata);
                    int coinsdatanew = coinsdataold +100;
                    String coinsdatanewString = String.valueOf(coinsdatanew);

                    bonus.child("coins").setValue(coinsdatanewString);


                    bonus.child("Bonus2").setValue("Yes");

                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String url = "https://www.facebook.com/krashunt/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);


    }


    @Override
    public void onRewarded(final RewardItem reward) {
       /* Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
                reward.getAmount(), Toast.LENGTH_SHORT).show();*/
        // Reward the user.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        myRef = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                final String coinsdata= dataSnapshot.getValue().toString();

                 int coinsdataold = Integer.parseInt(coinsdata);
                int Reward = reward.getAmount();

                int coinsdatanew = coinsdataold + Reward;
                String coinsdatanewString = String.valueOf(coinsdatanew);

                myRef.setValue(coinsdatanewString);






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        /*Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
       // Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
       // Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        //Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        //Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }


}
