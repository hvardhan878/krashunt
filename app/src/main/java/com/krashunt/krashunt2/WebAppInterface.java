package com.krashunt.krashunt2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by harsh on 10/9/2017.
 */

public class WebAppInterface {
    Context mContext;
    private String score;
   public static String uname="Harsh";
    //private String max = "0";
    public static Context contextOfApplication;
   private String pos;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef,productref,Ref2;
    private InterstitialAd mInterstitialAd;

private int location;

    WebAppInterface(Context ctx,String position, int intpos, String username){
        this.mContext=ctx;
        this.pos=position;
        Log.d("position",position);
        this.location = intpos;
        this.uname=username;
        this.score = "0";
    }
    public String getScore() {

        Log.d("get",score);
        return score;
    }
    public void setScore(String data){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();

        score = data;
        editor.putString("score",score);
        editor.apply();

    }


    @JavascriptInterface
    public void sendData(final String dat) {


       Log.d("score",dat);
        //Get the string value to process
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // Log.d("us",userID);
        final String posi = pos;
        // if(Integer.parseInt(max)<Integer.par
        myRef = mFirebaseDatabase.getReference();
        final int data = Integer.parseInt(dat);

        productref = mFirebaseDatabase.getReference().child("Product").child(posi).child(userID).child("Highscore");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("User").child(userID).child("username").getValue().toString();

                if(username!=null){

                    myRef.child("Product").child(posi).child(userID).child("username").setValue(username);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        productref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {





                if(dataSnapshot.getValue()==null)
                {
                    myRef.child("Product").child(posi).child(userID).child("Highscore").setValue(data);

                }
                else if(dataSnapshot.getValue()!=null){

                    String score = dataSnapshot.getValue().toString();
                    if(Integer.parseInt(score)<data)

                        myRef.child("Product").child(posi).child(userID).child("Highscore").setValue(data);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Log.d("position",this.pos);
       // Log.d("score",dat);
        if(this.location == 0){

            //Flappy Bird
            if(Integer.parseInt(dat)>5&&Integer.parseInt(dat)<=10) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 10));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>10&&Integer.parseInt(dat)<=20) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 30));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            if(Integer.parseInt(dat)>20&&Integer.parseInt(dat)<=30) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 60));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>30&&Integer.parseInt(dat)<=40) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 100));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>40&&Integer.parseInt(dat)<=45) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 150));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>45) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 220));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }




        }
        if(this.location == 1){

            //Stick Hero
            if(Integer.parseInt(dat)>0&&Integer.parseInt(dat)<=20) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 10));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>20&&Integer.parseInt(dat)<=30) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 30));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            if(Integer.parseInt(dat)>30&&Integer.parseInt(dat)<=40) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 60));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>40&&Integer.parseInt(dat)<=50) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 100));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>50&&Integer.parseInt(dat)<=60) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 150));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>60) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 220));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }




        }  if(this.location == 2){

            //2 Cars
            if(Integer.parseInt(dat)>20&&Integer.parseInt(dat)<=50) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 10));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>50&&Integer.parseInt(dat)<=100) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 30));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            if(Integer.parseInt(dat)>100&&Integer.parseInt(dat)<=150) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 60));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>150&&Integer.parseInt(dat)<=200) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 100));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>200&&Integer.parseInt(dat)<=250) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 150));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>250) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 220));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }




        }  if(this.location == 3){

            //Hextris
            if(Integer.parseInt(dat)>300&&Integer.parseInt(dat)<=400) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 10));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>400&&Integer.parseInt(dat)<=600) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 30));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            if(Integer.parseInt(dat)>600&&Integer.parseInt(dat)<=800) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 60));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>800&&Integer.parseInt(dat)<=1000) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 100));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>1000&&Integer.parseInt(dat)<=1200) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 150));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            if(Integer.parseInt(dat)>1200) {
                Ref2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");

                Ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentcoins = dataSnapshot.getValue().toString();

                        Ref2.setValue(String.valueOf(Integer.parseInt(currentcoins) + 220));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }






        //Log.d("Mytag",data);
    }

    @JavascriptInterface
    public void showAd(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // Log.d("us",userID);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        // if(Integer.parseInt(max)<Integer.par


        Game.ad(mContext);






    }
}
