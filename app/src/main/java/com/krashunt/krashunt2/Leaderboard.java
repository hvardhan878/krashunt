package com.krashunt.krashunt2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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
import java.util.Collections;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private String posi;

    public static  ViewHolder listViewHolder;


    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Intent mIntent = getIntent();
        posi = mIntent.getStringExtra("text");

        final ListView leaderboard = (ListView) findViewById(R.id.leaderlist);
        final List<leaderobject> allItems = new ArrayList<>();




        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //final String userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // Log.d("us",userID);
        myRef = mFirebaseDatabase.getReference().child("Product").child(posi);

        Query queryRef = myRef.orderByChild("Highscore").limitToLast(10);



        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 1;
                if(allItems!=null){

                    allItems.clear();
                }


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Score score=postSnapshot.getValue(Score.class);

                    String is = Integer.toString(i);
                    allItems.add(new leaderobject(is,score.getUsername(),String.valueOf(score.getHighscore())));

                    i++;



                    Log.d("test"," values is " +  score.getUsername() + " " + score.getHighscore());
                }
                Collections.reverse(allItems);
                CustomAdapter customAdapter = new CustomAdapter(Leaderboard.this, allItems);
                leaderboard.setAdapter(customAdapter);
                i=0;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void backproduct(View v){
        onBackPressed();

    }
    public static class ViewHolder {

        TextView position;

        TextView username;
        TextView highscore;


    }

    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater layoutinflater;
        private List<leaderobject> listStorage;
        private Context context;


        public CustomAdapter(Context context, List<leaderobject> customizedListView) {
            this.context = context;
            layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listStorage = customizedListView;
        }

        @Override
        public int getCount() {
            return listStorage.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if(convertView == null){
                listViewHolder = new ViewHolder();
                convertView = layoutinflater.inflate(R.layout.leaderboard, parent, false);
                listViewHolder.position = (TextView)convertView.findViewById(R.id.pos);
                listViewHolder.username = (TextView)convertView.findViewById(R.id.user);
                listViewHolder.highscore = (TextView)convertView.findViewById(R.id.score);

                convertView.setTag(listViewHolder);
            }else{
                listViewHolder = (ViewHolder)convertView.getTag();

            }
            //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

            listViewHolder.position.setText(String.valueOf(position+1));
            listViewHolder.username.setText(listStorage.get(position).getUsername());
            listViewHolder.highscore.setText(listStorage.get(position).getScore());
            // int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(),null,null);
            //listViewHolder.imageInListView.setImageResource(imageResourceId);

            return convertView;
        }



    }
}
