package com.krashunt.krashunt2;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailActivity extends AppCompatActivity {
int pos; String text;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private Receiver mInternetStatusNotifier;
    private boolean checkdeadline = false;

    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInternetStatusNotifier = new Receiver();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent mIntent = getIntent();
        String image = mIntent.getStringExtra("image");
        String image2 = mIntent.getStringExtra("image2");
        String cost = mIntent.getStringExtra("cost");
        String details = mIntent.getStringExtra("details");
        final String FinalDate = mIntent.getStringExtra("deadline");

      //  Log.d("image2",image2);
         text = mIntent.getStringExtra("text");
        pos = mIntent.getIntExtra("pos",0);
       final TextView highscore =(TextView) findViewById(R.id.scoretobeat);
        final TextView highestscorer=(TextView) findViewById(R.id.leader);
        String position = Integer.toString(pos);
        //Log.d("pos",pos);
        TextView myAwesomeTextView = (TextView)findViewById(R.id.productname);
       // TextView Cost = (TextView)findViewById(R.id.cost);
        TextView Details = (TextView)findViewById(R.id.details);

        TextView score1 = (TextView) findViewById(R.id.score1);
        TextView score2 = (TextView) findViewById(R.id.score2);
        TextView score3 = (TextView) findViewById(R.id.score3);
        TextView score4 = (TextView) findViewById(R.id.score4);
        TextView score5 = (TextView) findViewById(R.id.score5);
        TextView score6 = (TextView) findViewById(R.id.score6);

        if(pos==0)
        {
            score1.setText("5: ");
            score2.setText("10: ");
            score3.setText("20: ");
            score4.setText("30: ");
            score5.setText("40: ");
            score6.setText("45+: ");



        }

        if(pos==2)
        {
            score1.setText("20: ");
            score2.setText("50: ");
            score3.setText("100: ");
            score4.setText("150: ");
            score5.setText("200: ");
            score6.setText("250+: ");



        }
        if(pos==1)
        {
            score1.setText("10: ");
            score2.setText("20: ");
            score3.setText("30: ");
            score4.setText("40: ");
            score5.setText("50: ");
            score6.setText("60+: ");



        }
        if(pos==3)
        {
            score1.setText("300: ");
            score2.setText("400: ");
            score3.setText("600: ");
            score4.setText("800: ");
            score5.setText("1000: ");
            score6.setText("1200+: ");



        }










      /*  try {
            //Dates to compare
            long date = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String dateString = sdf.format(date);



            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");

            //Setting dates
            date1 = dates.parse(dateString);
            date2 = dates.parse(FinalDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);


            //Convert long to String
            String dayDifference = Long.toString(differenceDates);
            if(dayDifference.equals("0")){checkdeadline = true;}
            TextView deadl = (TextView) findViewById(R.id.deadline);


           if(!dayDifference.equals("1"))
           { //deadl.setText(dayDifference + " days");}
            else{//deadl.setText(dayDifference + " day");}






            Log.e("HERE","HERE: " + dayDifference+ difference);

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }*/
//String url = "https://graph.facebook.com/" + id + "/picture?type=large";
        myAwesomeTextView.setText(text);
    //   Cost.setText(cost);
       Details.setText(details);
       ImageView banner = (ImageView) findViewById(R.id.banner_slider2);
      //  BannerSlider bannerSlider = (BannerSlider) findViewById(R.id.banner_slider2);
       // List<Banner> banners=new ArrayList<>();
       // banners.add(new RemoteBanner(image));

       // banners.add(new RemoteBanner(image2));
        //add banner using resource drawable

       // bannerSlider.setBanners(banners);
        Glide.with(this)
                .load(image2)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .crossFade(200)
                .into(banner);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //final String userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // Log.d("us",userID);
        myRef = mFirebaseDatabase.getReference().child("Product").child(text);
/*
        Query queryRef = myRef.orderByChild("Highscore").limitToLast(5);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Score score=postSnapshot.getValue(Score.class);



                    Log.d("test"," values is " +  score.getUsername() + " " + score.getHighscore());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        Query queryRefhigh = myRef.orderByChild("Highscore").limitToLast(1);
        queryRefhigh.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Score score=postSnapshot.getValue(Score.class);

                    highscore.setText(String.valueOf(score.getHighscore()));
                    highestscorer.setText(score.getUsername());

                    //Log.d("test2"," values is " +  score.getUsername() + " " + score.getHighscore());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Button leaderboard = (Button) findViewById(R.id.lboard);
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent leader = new Intent(ProductDetailActivity.this,Leaderboard.class);
                leader.putExtra("text",text);
                startActivity(leader);



            }
        });



    }


    public void play(View v){
        if(checkdeadline==true){


            Toast.makeText(this, "Deadline Reached", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent I = new Intent(ProductDetailActivity.this, Game.class);
            I.putExtra("position", pos);
            I.putExtra("text", text);
            startActivity(I);
        }

    }
    public void backhome(View v){

        onBackPressed();
    }

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
