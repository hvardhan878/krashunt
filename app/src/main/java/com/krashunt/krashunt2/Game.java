package com.krashunt.krashunt2;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.inmobi.ads.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import im.delight.android.webview.AdvancedWebView;


public class Game extends AppCompatActivity implements AdvancedWebView.Listener{
    String score="0"; AdvancedWebView myWebView;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef,productref,Ref,myRef2;
    public static Context contextOfApplication;

    private TextView highscore;
    private Receiver mInternetStatusNotifier;
    String usern;
    public static InterstitialAd mInterstitialAd;
    public static InMobiInterstitial.InterstitialAdListener2 mInterstitialListener;
    public static InMobiInterstitial interstitialAd;
    public static boolean mCanShowAd = false;
    String prod;
    private String TAG = "interstitial";
    static Game g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInternetStatusNotifier = new Receiver();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        g = Game.this;



        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1407187642190765/1340788136");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }
        });
        // ‘this’ is used to specify context, replace it with the appropriate context as needed.



        // ‘this’ is used to specify context, replace it with the appropriate context as needed.



        //contextOfApplication = getApplicationContext();
        Intent mIntent = getIntent();
        final int pos = mIntent.getIntExtra("position", 0);
        final String text = mIntent.getStringExtra("text");
       // Log.d("pos",pos);
        final int product = pos;

        highscore = (TextView)findViewById(R.id.userscore);
        final TextView scoretobeat = (TextView)findViewById(R.id.bestscore);

        prod = Integer.toString(product);
        myWebView = (AdvancedWebView) findViewById(R.id.webview);
        myWebView.setListener(this, this);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
       final String userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
       // Log.d("us",userID);
        myRef = mFirebaseDatabase.getReference().child("User").child(userID).child("username");
        productref =mFirebaseDatabase.getReference().child("Product").child(text).child(userID).child("Highscore");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myRef2 = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                final String coinsdata= dataSnapshot.getValue().toString();

                TextView coins = (TextView) findViewById(R.id.coins);

                coins.setText(coinsdata);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                final String username = dataSnapshot.getValue().toString();
                WebAppInterface w = new WebAppInterface(Game.this,text,pos,username);
                myWebView.addJavascriptInterface(w,"Android");




                System.out.println("Data saved successfully.");



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //score = sharedPref.getString("score","Not Available");
        productref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    String userscore = dataSnapshot.getValue().toString();

                    highscore.setText(userscore);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       if(pos==0)
       {myWebView.loadUrl("file:///android_asset/flappybird/index.html");
          // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
          // myRef.child("Product").child("1").child("Harsh").child("Highscore").setValue("5");

       }


        else if(pos==1)
        {myWebView.loadUrl("file:///android_asset/stickhero/index.html");}


        else if(pos==2)
        {
            myWebView.loadUrl("file:///android_asset/2Cars/index.html");
        }

       else if(pos==3)

       {
           myWebView.loadUrl("file:///android_asset/bottleflip/www/index.html");
          //myWebView.loadUrl("file:///android_asset/hextris/index.html");
          // myWebView.loadUrl("http://bit.ly/2GXUtE7");

       }

       else if(pos==4)
       {
         // myWebView.loadUrl("file:///android_asset/bottleflip/index.html");
       }
           //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);




        Ref = mFirebaseDatabase.getReference().child("Product").child(text);

        Query queryRefhigh = Ref.orderByChild("Highscore").limitToLast(1);
        queryRefhigh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Score score=postSnapshot.getValue(Score.class);

                   scoretobeat.setText(String.valueOf(score.getHighscore()));


                    //Log.d("test"," values is " +  score.getUsername() + " " + score.getHighscore());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



     // Log.d("my score",score);




    }

    public static void ad(final Context context){




       // Log.d("over",String.valueOf(over));








        g.runOnUiThread(new Runnable() {
            public void run() {
                //if(mCanShowAd)
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        });
    }



    @Override
    protected void onDestroy() {
        myWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        myWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!myWebView.onBackPressed()) { return; }
        // ...
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Game.this);
        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();

        score = "0";
        editor.putString("score",score);
        editor.apply();
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

    @Override
    protected void onResume() {
        registerReceiver(mInternetStatusNotifier, new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mInternetStatusNotifier);
        super.onPause();
    }



}
