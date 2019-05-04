package com.krashunt.krashunt2;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class profileactivity extends AppCompatActivity {
    boolean check_facebook=false;
    private static int RESULT_LOAD_IMAGE = 1;
    int MY_READ_PERMISSION_REQUEST_CODE =1 ;
    int PICK_IMAGE_REQUEST = 2;
    private Receiver mInternetStatusNotifier;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef,userphoto;
   private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInternetStatusNotifier = new Receiver();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileactivity);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser usercoin = mAuth.getCurrentUser();
       userID = usercoin.getUid();
        myRef = mFirebaseDatabase.getReference().child("User").child(userID).child("coins");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                final String coinsdata= dataSnapshot.getValue().toString();

                TextView coins = (TextView) findViewById(R.id.coins);

                coins.setText(coinsdata);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {

            if (user.getProviderId().equals("facebook.com")) {
                //For linked facebook account
                check_facebook=true;
                TextView profile = (TextView)findViewById(R.id.edit);
                profile.setVisibility(View.GONE);
                Log.d("xx_xx_provider_info", "User is signed in with Facebook");
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String fName = sharedPref.getString("firstname", "Not Available");
                String id = sharedPref.getString("id", null);
                TextView myAwesomeTextView = (TextView)findViewById(R.id.nameview);
                Log.d("Id",id);
//String url = "https://graph.facebook.com/" + id + "/picture?type=large";
                myAwesomeTextView.setText(fName);
                ImageView pro =(ImageView)findViewById(R.id.profile_picture);
                Glide.with(profileactivity.this)
                        .load("https://graph.facebook.com/" +id+ "/picture?type=large")
                        .into(pro);



            }
            else{

              TextView profile = (TextView)findViewById(R.id.edit);


               // SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
              //  String pic = sharedPref.getString("picture", "Not Available");
              //  Log.d("pic",pic);
                profile.setVisibility(View.VISIBLE);


                mFirebaseDatabase = FirebaseDatabase.getInstance();


               DatabaseReference user1 = mFirebaseDatabase.getReference().child("User").child(userID);
                user1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("userphoto").getValue()!=null){
                        String userphoto = dataSnapshot.child("userphoto").getValue().toString();
                        ImageView imageview=(ImageView)findViewById(R.id.profile_picture);
                        Glide.with(profileactivity.this)
                                .load(String.valueOf(userphoto))
                                .into(imageview);}

                        TextView myAwesomeTextView = (TextView)findViewById(R.id.nameview);

//String url = "https://graph.facebook.com/" + id + "/picture?type=large";
                        if(dataSnapshot.child("username").getValue()!=null)
                        {myAwesomeTextView.setText(dataSnapshot.child("username").getValue().toString());}
                        else{

                            myAwesomeTextView.setText(dataSnapshot.child("email").getValue().toString());
                        }




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






                // Bitmap picture = StringToBitMap(pic);

               // ImageView imageview=(ImageView)findViewById(R.id.profile_picture);
                //imageview.setImageBitmap(picture);
                profile.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {

                        if(check_facebook==false)
                        { CHECKGALLERYPERMISSION();}
                    }


                });




            }

        }




    }


    private void CHECKGALLERYPERMISSION()
    {

        if (ContextCompat.checkSelfPermission(profileactivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_READ_PERMISSION_REQUEST_CODE);
            }
            else
            {
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        if (requestCode == MY_READ_PERMISSION_REQUEST_CODE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri uri = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(profileactivity.this.getContentResolver(), uri);

                uploadFile(bitmap);


                ImageView imageview=(ImageView)findViewById(R.id.profile_picture);

               imageview.setImageBitmap(bitmap);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    public void backprofile(View v){

        onBackPressed();
 }

    private void uploadFile(Bitmap bitmap) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://krashunt-8ebf7.appspot.com/");
        StorageReference mountainImagesRef = storageRef.child("images/" + userID + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
               // sendMsg("" + downloadUrl, 2);
                Log.d("downloadUrl-->", "" + downloadUrl);
                mFirebaseDatabase = FirebaseDatabase.getInstance();

                userphoto = mFirebaseDatabase.getReference();
                userphoto.child("User").child(userID).child("userphoto").setValue(downloadUrl.toString());



            }
        });

    }
public void tandc(View v){
     Intent terms = new Intent(getBaseContext(),terms.class);
    startActivity(terms);


}
    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
     Intent logout = new Intent(profileactivity.this,LoginActivity.class);
        startActivity(logout);

    }
  public void work(View v){

      Intent I = new Intent(getBaseContext(),WelcomeActivity.class);

      startActivity(I);

  }
  public void about(View v){

      String url = "http://www.krashunt.com";
      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(url));
      startActivity(i);
  }

  public void contact(View v){

      Intent intent= new Intent(getBaseContext(),ContactActivity.class);
      startActivity(intent);

  }
  public void location(View v){

      Intent intent= new Intent(getBaseContext(),LocationActivity.class);
      startActivity(intent);
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
