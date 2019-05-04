package com.krashunt.krashunt2.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import com.krashunt.krashunt2.OfferObject;
import com.krashunt.krashunt2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Offers.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Offers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Offers extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String numWinner = "0";
    private String mParam2;
    private Query query;
    private String Offers = "Offers";
    private ViewHolder listViewHolder;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef,loc,purchase,stock,offerlocation;
   private  FirebaseRecyclerAdapter<OfferObject,ViewHolder> adapter;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private SwipeRefreshLayout swipeLayout;

    private static Bundle mBundleRecyclerViewState;
    //private Button coinbuy;
    //private ImageView unavailable;
    //private TextView unavailabletext;
    private boolean bool = false;
    ListView redeem;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private OnFragmentInteractionListener mListener;

    public Offers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Offers.
     */
    // TODO: Rename and change types and number of parameters
    public static Offers newInstance() {
        Offers fragment = new Offers();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     final View view1 = inflater.inflate(R.layout.fragment_offers, container, false);
         //redeem = (ListView) view.findViewById(R.id.listview);
        recyclerView =(RecyclerView) view1.findViewById(R.id.listview);
        final List<OfferObject> allItem =new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);

        mFirebaseDatabase = FirebaseDatabase.getInstance();


     //  offerlocation = mFirebaseDatabase.getReference().child("User").child(userID).child("location");


        swipeLayout = (SwipeRefreshLayout) view1.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(Offers.this);




        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String check = preferences.getString("Location", "");

        if(check.equals("Hong Kong"))
        {
            Log.d("offerloop","offerloop");

            Offers = "Offers";
            Log.d("Offers",Offers);
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Offers);


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://krashunt-8ebf7.firebaseio.com/Offers");
            FirebaseRecyclerOptions<OfferObject> options = new FirebaseRecyclerOptions.Builder<OfferObject>().setQuery(query, OfferObject.class).build();


            adapter = new FirebaseRecyclerAdapter<OfferObject, ViewHolder>(

                    options
            ) {
                @Override
                protected void onBindViewHolder(ViewHolder listViewHolder, int position, OfferObject listStorage) {


                    Log.d("tagcheck", String.valueOf(position));


                    //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
                    listViewHolder.termsandcon.setTag(position);
                    listViewHolder.termsandcon.setOnClickListener(termsandcon);

                    listViewHolder.coinbuy.setTag(position);
                    listViewHolder.coinbuy.setOnClickListener(likeButtonClickListener);

                    if (listStorage.getStock().equals("unlimited")) {
                        listViewHolder.stock.setText("");
                        listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.INVISIBLE);
                        listViewHolder.unavailable.setVisibility(listViewHolder.convertView.INVISIBLE);
                        Glide.with(getActivity())
                                .load(listStorage.getImageResource())
                                .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .fitCenter()
                                .crossFade(200)
                                .into(listViewHolder.imageInListView);


                    } else if (listStorage.getStock().equals("0")) {
                        listViewHolder.stock.setText("Not In Stock");

                        listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.VISIBLE);
                        listViewHolder.unavailable.setVisibility(listViewHolder.convertView.VISIBLE);

                        Glide.with(getActivity())
                                .load(listStorage.getImageResource())
                                .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .fitCenter()
                                .crossFade(200)
                                .into(listViewHolder.imageInListView);


                    } else {
                        listViewHolder.stock.setText("In Stock: " + listStorage.getStock());
                        listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.INVISIBLE);
                        listViewHolder.unavailable.setVisibility(listViewHolder.convertView.INVISIBLE);

                        Glide.with(getActivity())
                                .load(listStorage.getImageResource())
                                .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .fitCenter()
                                .crossFade(200)
                                .into(listViewHolder.imageInListView);

                    }
                    listViewHolder.textInListView.setText(listStorage.getContent());
                    listViewHolder.costcoin.setText(String.valueOf(listStorage.getCost()));
                    // int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(),null,null);
                    //listViewHolder.imageInListView.setImageResource(imageResourceId);


                }

                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    Log.d("check", "check");

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewoffer, parent, false);

                    return new ViewHolder(view);
                }
            };
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                    layoutManager.findLastCompletelyVisibleItemPosition();
                    // If the recycler view is initially being loaded or the
                    // user is at the bottom of the list, scroll to the bottom
                    // of the list to show the newly added message.

                }
            });


            recyclerView.setAdapter(adapter);
            bool = true;


        }

        if(check.equals("India"))
        {

            Offers = "OffersIndia";
            Log.d("Offers",Offers);
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Offers);


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://krashunt-8ebf7.firebaseio.com/Offers");
            FirebaseRecyclerOptions<OfferObject> options = new FirebaseRecyclerOptions.Builder<OfferObject>().setQuery(query, OfferObject.class).build();


            adapter = new FirebaseRecyclerAdapter<OfferObject, ViewHolder>(

                    options
            ) {
                @Override
                protected void onBindViewHolder(ViewHolder listViewHolder, int position, OfferObject listStorage) {


                    Log.d("tagcheck", String.valueOf(position));


                    //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
                    listViewHolder.termsandcon.setTag(position);
                    listViewHolder.termsandcon.setOnClickListener(termsandcon);

                    listViewHolder.coinbuy.setTag(position);
                    listViewHolder.coinbuy.setOnClickListener(likeButtonClickListener);

                    if (listStorage.getStock().equals("unlimited")) {
                        listViewHolder.stock.setText("");
                        listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.INVISIBLE);
                        listViewHolder.unavailable.setVisibility(listViewHolder.convertView.INVISIBLE);
                        Glide.with(getActivity())
                                .load(listStorage.getImageResource())
                                .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .fitCenter()
                                .crossFade(200)
                                .into(listViewHolder.imageInListView);


                    } else if (listStorage.getStock().equals("0")) {
                        listViewHolder.stock.setText("Not In Stock");

                        listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.VISIBLE);
                        listViewHolder.unavailable.setVisibility(listViewHolder.convertView.VISIBLE);

                        Glide.with(getActivity())
                                .load(listStorage.getImageResource())
                                .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .fitCenter()
                                .crossFade(200)
                                .into(listViewHolder.imageInListView);


                    } else {
                        listViewHolder.stock.setText("In Stock: " + listStorage.getStock());
                        listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.INVISIBLE);
                        listViewHolder.unavailable.setVisibility(listViewHolder.convertView.INVISIBLE);

                        Glide.with(getActivity())
                                .load(listStorage.getImageResource())
                                .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .fitCenter()
                                .crossFade(200)
                                .into(listViewHolder.imageInListView);

                    }
                    listViewHolder.textInListView.setText(listStorage.getContent());
                    listViewHolder.costcoin.setText(String.valueOf(listStorage.getCost()));
                    // int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(),null,null);
                    //listViewHolder.imageInListView.setImageResource(imageResourceId);


                }

                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    Log.d("check", "check");

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewoffer, parent, false);

                    return new ViewHolder(view);
                }
            };
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);

                    layoutManager.findLastCompletelyVisibleItemPosition();
                    // If the recycler view is initially being loaded or the
                    // user is at the bottom of the list, scroll to the bottom
                    // of the list to show the newly added message.

                }
            });


            recyclerView.setAdapter(adapter);

            bool = true;

        }

/*

//whatever you want to get ,get it here.
//for example integer given
     offerlocation.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(DataSnapshot dataSnapshot) {
             Log.d("Offers",dataSnapshot.getValue().toString());
             String check = dataSnapshot.getValue().toString();


             if(check.equals("Hong Kong"))
             {
                 Log.d("offerloop","offerloop");

                 Offers = "Offers";
                 Log.d("Offers",Offers);
                 query = FirebaseDatabase.getInstance()
                         .getReference()
                         .child(Offers);


                 DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://krashunt-8ebf7.firebaseio.com/Offers");
                 FirebaseRecyclerOptions<OfferObject> options = new FirebaseRecyclerOptions.Builder<OfferObject>().setQuery(query, OfferObject.class).build();


                 adapter = new FirebaseRecyclerAdapter<OfferObject, ViewHolder>(

                         options
                 ) {
                     @Override
                     protected void onBindViewHolder(ViewHolder listViewHolder, int position, OfferObject listStorage) {


                         Log.d("tagcheck", String.valueOf(position));


                         //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
                         listViewHolder.termsandcon.setTag(position);
                         listViewHolder.termsandcon.setOnClickListener(termsandcon);

                         listViewHolder.coinbuy.setTag(position);
                         listViewHolder.coinbuy.setOnClickListener(likeButtonClickListener);

                         if (listStorage.getStock().equals("unlimited")) {
                             listViewHolder.stock.setText("");
                             listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.INVISIBLE);
                             listViewHolder.unavailable.setVisibility(listViewHolder.convertView.INVISIBLE);
                             Glide.with(getActivity())
                                     .load(listStorage.getImageResource())
                                     .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                     .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                     .fitCenter()
                                     .crossFade(200)
                                     .into(listViewHolder.imageInListView);


                         } else if (listStorage.getStock().equals("0")) {
                             listViewHolder.stock.setText("Not In Stock");

                             listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.VISIBLE);
                             listViewHolder.unavailable.setVisibility(listViewHolder.convertView.VISIBLE);

                             Glide.with(getActivity())
                                     .load(listStorage.getImageResource())
                                     .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                     .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                     .fitCenter()
                                     .crossFade(200)
                                     .into(listViewHolder.imageInListView);


                         } else {
                             listViewHolder.stock.setText("In Stock: " + listStorage.getStock());
                             listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.INVISIBLE);
                             listViewHolder.unavailable.setVisibility(listViewHolder.convertView.INVISIBLE);

                             Glide.with(getActivity())
                                     .load(listStorage.getImageResource())
                                     .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                     .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                     .fitCenter()
                                     .crossFade(200)
                                     .into(listViewHolder.imageInListView);

                         }
                         listViewHolder.textInListView.setText(listStorage.getContent());
                         listViewHolder.costcoin.setText(String.valueOf(listStorage.getCost()));
                         // int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(),null,null);
                         //listViewHolder.imageInListView.setImageResource(imageResourceId);


                     }

                     @Override
                     public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                         Log.d("check", "check");

                         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewoffer, parent, false);

                         return new ViewHolder(view);
                     }
                 };
                 adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                     @Override
                     public void onItemRangeInserted(int positionStart, int itemCount) {
                         super.onItemRangeInserted(positionStart, itemCount);

                         layoutManager.findLastCompletelyVisibleItemPosition();
                         // If the recycler view is initially being loaded or the
                         // user is at the bottom of the list, scroll to the bottom
                         // of the list to show the newly added message.

                     }
                 });


                 recyclerView.setAdapter(adapter);
                 bool = true;


             }

             if(check.equals("India"))
             {

                 Offers = "OffersIndia";
                 Log.d("Offers",Offers);
                 query = FirebaseDatabase.getInstance()
                         .getReference()
                         .child(Offers);


                 DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://krashunt-8ebf7.firebaseio.com/Offers");
                 FirebaseRecyclerOptions<OfferObject> options = new FirebaseRecyclerOptions.Builder<OfferObject>().setQuery(query, OfferObject.class).build();


                 adapter = new FirebaseRecyclerAdapter<OfferObject, ViewHolder>(

                         options
                 ) {
                     @Override
                     protected void onBindViewHolder(ViewHolder listViewHolder, int position, OfferObject listStorage) {


                         Log.d("tagcheck", String.valueOf(position));


                         //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
                         listViewHolder.termsandcon.setTag(position);
                         listViewHolder.termsandcon.setOnClickListener(termsandcon);

                         listViewHolder.coinbuy.setTag(position);
                         listViewHolder.coinbuy.setOnClickListener(likeButtonClickListener);

                         if (listStorage.getStock().equals("unlimited")) {
                             listViewHolder.stock.setText("");
                             listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.INVISIBLE);
                             listViewHolder.unavailable.setVisibility(listViewHolder.convertView.INVISIBLE);
                             Glide.with(getActivity())
                                     .load(listStorage.getImageResource())
                                     .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                     .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                     .fitCenter()
                                     .crossFade(200)
                                     .into(listViewHolder.imageInListView);


                         } else if (listStorage.getStock().equals("0")) {
                             listViewHolder.stock.setText("Not In Stock");

                             listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.VISIBLE);
                             listViewHolder.unavailable.setVisibility(listViewHolder.convertView.VISIBLE);

                             Glide.with(getActivity())
                                     .load(listStorage.getImageResource())
                                     .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                     .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                     .fitCenter()
                                     .crossFade(200)
                                     .into(listViewHolder.imageInListView);


                         } else {
                             listViewHolder.stock.setText("In Stock: " + listStorage.getStock());
                             listViewHolder.unavailabletext.setVisibility(listViewHolder.convertView.INVISIBLE);
                             listViewHolder.unavailable.setVisibility(listViewHolder.convertView.INVISIBLE);

                             Glide.with(getActivity())
                                     .load(listStorage.getImageResource())
                                     .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                                     .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                     .fitCenter()
                                     .crossFade(200)
                                     .into(listViewHolder.imageInListView);

                         }
                         listViewHolder.textInListView.setText(listStorage.getContent());
                         listViewHolder.costcoin.setText(String.valueOf(listStorage.getCost()));
                         // int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(),null,null);
                         //listViewHolder.imageInListView.setImageResource(imageResourceId);


                     }

                     @Override
                     public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                         Log.d("check", "check");

                         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listviewoffer, parent, false);

                         return new ViewHolder(view);
                     }
                 };
                 adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                     @Override
                     public void onItemRangeInserted(int positionStart, int itemCount) {
                         super.onItemRangeInserted(positionStart, itemCount);

                         layoutManager.findLastCompletelyVisibleItemPosition();
                         // If the recycler view is initially being loaded or the
                         // user is at the bottom of the list, scroll to the bottom
                         // of the list to show the newly added message.

                     }
                 });


                 recyclerView.setAdapter(adapter);

                 bool = true;

             }


           //return view1;
          // adapter.notifyDataSetChanged();


         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }


     });

*/









       // final int index = redeem.getFirstVisiblePosition();
       // View v = redeem.getChildAt(0);
       // final int top = (v == null) ? 0 : (v.getTop() - redeem.getPaddingTop());

       /* myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(allItem!=null){

                    allItem.clear();
                }



                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    OfferObject offerobject = postSnapshot.getValue(OfferObject.class);
                    String Location = offerobject.getLocation();


                    String userloc = getUserCountry(getContext());
                    Log.d("loc", userloc);
                    Log.d("loca", Location);


                    // Score score=postSnapshot.getValue(Score.class);


                    if (Location.equals(userloc) || Location == null)

                    {
                        allItem.add(new OfferObject(offerobject.getCost(), offerobject.getContent(), offerobject.getImageResource(), offerobject.getLocation(), offerobject.getStock()));
                    }


                    CustomAdapter customAdapter = new CustomAdapter(getActivity(), allItem);
                    redeem.setAdapter(customAdapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
/*
       myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {




                    //Log.d("test"," values is " +  score.getUsername() + " " + score.getHighscore());
                OfferObject offerobject = dataSnapshot.getValue(OfferObject.class);
                String Location = offerobject.getLocation();


                String userloc = getUserCountry(getContext());
                Log.d("loc", userloc);
                Log.d("loca", Location);


                // Score score=postSnapshot.getValue(Score.class);


                if (Location.equals(userloc) || Location == null)

                {
                    allItem.add(new OfferObject(offerobject.getCost(), offerobject.getContent(), offerobject.getImageResource(), offerobject.getLocation(), offerobject.getStock()));
                }


                CustomAdapter customAdapter = new CustomAdapter(getActivity(), allItem);
                redeem.setAdapter(customAdapter);



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {




            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });*/




       /* redeem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        return view;*/
     // while(bool==false){}


           return view1;


    }





    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
         adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        //adapter.stopListening();
    }
    @Override
    public void onPause() {
        //adapter.stopListening();
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
          recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        if(adapter!=null)
            adapter.startListening();
    }


    public void onRefresh() {
        Log.d("Refreshing","The refresh is working");

        getActivity().finish();
        startActivity(getActivity().getIntent());
        swipeLayout.setRefreshing(false);
    }




    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }

        }
        catch (Exception e) { }
        return null;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView costcoin;
       TextView stock;
        ImageView unavailable;
        TextView unavailabletext;
        Button coinbuy;
        View convertView;
        TextView termsandcon;


        TextView textInListView;
        ImageView imageInListView;


        public ViewHolder(View convertView){
            super(convertView);
           textInListView = (TextView) convertView.findViewById(R.id.textView);
           imageInListView = (ImageView) convertView.findViewById(R.id.listimageView);
          costcoin = (TextView) convertView.findViewById(R.id.costcoins);
            stock = (TextView) convertView.findViewById(R.id.remain);
            unavailable=(ImageView) convertView.findViewById(R.id.notinstock);
            unavailabletext = (TextView) convertView.findViewById(R.id.notinstocktext);
            coinbuy =(Button) convertView.findViewById(R.id.buttonbuy);
            termsandcon = (TextView) convertView.findViewById(R.id.coupondetails);
            termsandcon.setPaintFlags(termsandcon.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

             this.convertView = convertView;


        }

    }

    private View.OnClickListener termsandcon = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int position = (Integer) view.getTag();

            DatabaseReference terms;
            terms = mFirebaseDatabase.getReference().child(Offers).child(String.valueOf(position)).child("terms");
            terms.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    String tc =dataSnapshot.getValue().toString();
                    if(dataSnapshot.getValue()!=null)
                    { tc = dataSnapshot.getValue().toString();}

                    builder.setTitle("Terms and Conditions");
                    builder.setMessage(tc);



                    builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    };



    private View.OnClickListener likeButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int position = (Integer) v.getTag();
            //Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
            FirebaseUser user = mAuth.getCurrentUser();
            final String userID = user.getUid();
            purchase = mFirebaseDatabase.getReference();
            stock = mFirebaseDatabase.getReference().child(Offers).child(String.valueOf(position)).child("stock");

            purchase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    String costoffer = dataSnapshot.child(Offers).child(String.valueOf(position)).child("cost").getValue().toString();
                    String usercoin= dataSnapshot.child("User").child(userID).child("coins").getValue().toString();

                    final String stockdata = dataSnapshot.child(Offers).child(String.valueOf(position)).child("stock").getValue().toString();

                    final String offer = dataSnapshot.child(Offers).child(String.valueOf(position)).child("content").getValue().toString();
                    final String userEmail = dataSnapshot.child("User").child(userID).child("email").getValue().toString();
                    final int coupon = Integer.parseInt(costoffer);

                    final int coin = Integer.parseInt(usercoin);
                    Log.d("coupon",costoffer);
                    Log.d("coin",usercoin);

                    if(stockdata.equals("0")){
                        Toast.makeText(getActivity(), "Not in Stock",  Toast.LENGTH_SHORT).show();

                    }

                    else if(coin<coupon){

                        Toast.makeText(getActivity(), "Not Enough Coins ",  Toast.LENGTH_SHORT).show();

                    }

                    else if(coin>=coupon){

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final EditText input = new EditText(getActivity());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);


                        builder.setTitle("Buy for "+ coupon +" coins");
                        builder.setMessage("Please enter your email address. An email will be sent to you with the details of the offer");
                        builder.setView(input);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing but close the dialog
                                String email = input.getText().toString();
                                purchase.child("User").child(userID).child("coins").setValue(String.valueOf(coin-coupon));
                                purchase.child("OfferWinners").child(offer).child(userID).child("email").setValue(email);

                                if(dataSnapshot.child("OfferWinners").child(offer).child(userID).child("wontime").getValue()!=null)
                                { numWinner = dataSnapshot.child("OfferWinners").child(offer).child(userID).child("wontime").getValue().toString();}
                                else  {
                                    purchase.child("OfferWinners").child(offer).child(userID).child("wontime").setValue("0");

                                }

                                purchase.child("OfferWinners").child(offer).child(userID).child("wontime").setValue(String.valueOf(Integer.parseInt(numWinner)+1));



                                stock.runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData mutableData) {

                                        mutableData.setValue(String.valueOf(Integer.valueOf(stockdata)-1));




                                        return Transaction.success(mutableData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                    }
                                });

                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();




                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });






        }
    };
    /*
    public class CustomAdapter extends BaseAdapter {

        private LayoutInflater layoutinflater;
        private List<OfferObject> listStorage;
        private Context context;



        public CustomAdapter(Context context, List<OfferObject> customizedListView) {
            this.context = context;
            layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listStorage = customizedListView;
        }
        private View.OnClickListener likeButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = (Integer) v.getTag();
                //Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();
                FirebaseUser user = mAuth.getCurrentUser();
                final String userID = user.getUid();
               purchase = mFirebaseDatabase.getReference();
                stock = mFirebaseDatabase.getReference().child("Offers").child(String.valueOf(position)).child("stock");

                purchase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                          String costoffer = dataSnapshot.child("Offers").child(String.valueOf(position)).child("cost").getValue().toString();
                        String usercoin= dataSnapshot.child("User").child(userID).child("coins").getValue().toString();
                        final String stockdata = dataSnapshot.child("Offers").child(String.valueOf(position)).child("stock").getValue().toString();

                        final String offer = dataSnapshot.child("Offers").child(String.valueOf(position)).child("content").getValue().toString();
                        final String userEmail = dataSnapshot.child("User").child(userID).child("email").getValue().toString();
                       final int coupon = Integer.parseInt(costoffer);

                        final int coin = Integer.parseInt(usercoin);
                        Log.d("coupon",costoffer);
                        Log.d("coin",usercoin);

                        if(stockdata.equals("0")){
                            Toast.makeText(getActivity(), "Not in Stock",  Toast.LENGTH_SHORT).show();

                        }

                        else if(coin<coupon){

                            Toast.makeText(getActivity(), "Not Enough Coins ",  Toast.LENGTH_SHORT).show();

                        }

                        else if(coin>=coupon){

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setTitle("Buy for "+ coupon +" coins");
                            builder.setMessage("An email will be sent to you with the details of the offer");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing but close the dialog
                                    purchase.child("User").child(userID).child("coins").setValue(String.valueOf(coin-coupon));
                                    purchase.child("OfferWinners").child(offer).child(userID).child("email").setValue(userEmail);


                                    stock.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {

                                            mutableData.setValue(String.valueOf(Integer.valueOf(stockdata)-1));




                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                        }
                                    });

                                    dialog.dismiss();
                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();




                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






            }
        };

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
        public View getView(final int position, View convertView, ViewGroup parent) {



            if (convertView == null) {
                listViewHolder = new ViewHolder();
                convertView = layoutinflater.inflate(R.layout.listviewoffer, parent, false);
                listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.textView);
                listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.listimageView);
                listViewHolder.costcoin = (TextView) convertView.findViewById(R.id.costcoins);
                listViewHolder.stock = (TextView) convertView.findViewById(R.id.remain);



                convertView.setTag(listViewHolder);
            } else {
                listViewHolder = (ViewHolder) convertView.getTag();

            }
            //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
          unavailable=(ImageView) convertView.findViewById(R.id.notinstock);
            unavailabletext = (TextView) convertView.findViewById(R.id.notinstocktext);
            coinbuy =(Button) convertView.findViewById(R.id.buttonbuy);
            coinbuy.setTag(position);
            coinbuy.setOnClickListener(likeButtonClickListener);

            if(listStorage.get(position).getStock().equals("unlimited")){
                listViewHolder.stock.setText("");
                unavailabletext.setVisibility(convertView.INVISIBLE);
                unavailable.setVisibility(convertView.INVISIBLE);
                Glide.with(getActivity())
                        .load(listStorage.get(position).getImageResource())
                        .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .crossFade(200)
                        .into(listViewHolder.imageInListView);






            }
            else if(listStorage.get(position).getStock().equals("0")){
                listViewHolder.stock.setText("Not In Stock");

                unavailabletext.setVisibility(convertView.VISIBLE);
            unavailable.setVisibility(convertView.VISIBLE);

                Glide.with(getActivity())
                        .load(listStorage.get(position).getImageResource())
                        .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .crossFade(200)
                        .into(listViewHolder.imageInListView);



            }

            else
            {  listViewHolder.stock.setText("In Stock: "+listStorage.get(position).getStock());
                unavailabletext.setVisibility(convertView.INVISIBLE);
                unavailable.setVisibility(convertView.INVISIBLE);

                Glide.with(getActivity())
                        .load(listStorage.get(position).getImageResource())
                        .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .crossFade(200)
                        .into(listViewHolder.imageInListView);

            }
            listViewHolder.textInListView.setText(listStorage.get(position).getContent());
            listViewHolder.costcoin.setText(listStorage.get(position).getCost());
            // int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(),null,null);
            //listViewHolder.imageInListView.setImageResource(imageResourceId);



            return convertView;


        }
    }*/
}
