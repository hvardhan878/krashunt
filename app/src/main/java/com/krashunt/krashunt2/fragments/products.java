package com.krashunt.krashunt2.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.krashunt.krashunt2.ItemObject;
import com.krashunt.krashunt2.ProductDetailActivity;
import com.krashunt.krashunt2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link products.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link products#newInstance} factory method to
 * create an instance of this fragment.
 */
public class products extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   // public static ViewHolder listViewHolder;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private RecyclerView gridview;
    private LinearLayoutManager layoutManager;
    //private FirebaseRecyclerAdapter<ItemObject,ViewHolder> adapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
   private List<ItemObject> allItems =new ArrayList<>();

    public products() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment products.
     */
    // TODO: Rename and change types and number of parameters
    public static products newInstance() {
        products fragment = new products();

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
     View view = inflater.inflate(R.layout.fragment_products, container, false);
        gridview = (RecyclerView) view.findViewById(R.id.gridview);
        gridview.setNestedScrollingEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        // Log.d("us",userID);
        myRef = mFirebaseDatabase.getReference().child("ProductDes");


       // gridview.setLayoutManager(new GridLayoutManager(getContext(), 2));
       // gridview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        layoutManager = new LinearLayoutManager(getContext());

        gridview.setLayoutManager(layoutManager);
        //setHeader(view);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(allItems!=null){

                    allItems.clear();
                }



                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // Score score=postSnapshot.getValue(Score.class);
                    ItemObject itemobject = postSnapshot.getValue(ItemObject.class);

                    allItems.add(new ItemObject(itemobject.getCost(),itemobject.getDetails(),itemobject.getContent(),itemobject.getImageResource(),itemobject.getImageResourcee(),itemobject.getDeadline()));





                    //Log.d("test"," values is " +  score.getUsername() + " " + score.getHighscore());
                }

               RecyclerViewAdapter customAdapter = new RecyclerViewAdapter(getActivity(), allItems);
               // customAdapter.setClickListener(this);
                gridview.setAdapter(customAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), ProductDetailActivity.class);
                myIntent.putExtra("image",allItems.get(position).getImageResource());
                // Log.d("image2",allItems.get(position).getImageResource2());
                myIntent.putExtra("image2",allItems.get(position).getImageResourcee());
                myIntent.putExtra("details",allItems.get(position).getDetails());
                myIntent.putExtra("cost",allItems.get(position).getCost());
                myIntent.putExtra("text",allItems.get(position).getContent());
                myIntent.putExtra("pos",position);
                startActivity(myIntent);
                // Toast.makeText(MainActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
            }
        });*/
    return view;

    }

    /*@Override
    public void onItemClick(View view,int position){
        Intent myIntent = new Intent(getActivity(), ProductDetailActivity.class);


        myIntent.putExtra("image",allItems.get(position).getImageResource());
        // Log.d("image2",allItems.get(position).getImageResource2());
        myIntent.putExtra("image2",allItems.get(position).getImageResourcee());
        myIntent.putExtra("details",allItems.get(position).getDetails());
        myIntent.putExtra("cost",allItems.get(position).getCost());
        myIntent.putExtra("text",allItems.get(position).getContent());
        myIntent.putExtra("pos",position);
        startActivity(myIntent);



    }*/




    @SuppressLint({"InflateParams", "SetTextI18n"})
    /*private void setHeader(View view){

       // LayoutInflater layoutInflater = LayoutInflater.from(getContext());
       View headerView = view.findViewById(R.id.banner);
        BannerSlider bannerSlider = (BannerSlider) headerView.findViewById(R.id.banner_slider1);
        List<Banner> banners=new ArrayList<>();
        //add banner using image url
        banners.add(new RemoteBanner("https://firebasestorage.googleapis.com/v0/b/krashunt-8ebf7.appspot.com/o/Samsung%20Charm%20Lifestyle%20Fitness2.png?alt=media&token=459d8be1-b48e-48d0-a9af-5f2ec7937076"));
        banners.add(new RemoteBanner("https://firebasestorage.googleapis.com/v0/b/krashunt-8ebf7.appspot.com/o/LeEco%20Bluetooth%20Speaker2.jpg?alt=media&token=f1176d44-4ce1-45c7-ae9e-6a0e4566ad50"));
        banners.add(new RemoteBanner("https://firebasestorage.googleapis.com/v0/b/krashunt-8ebf7.appspot.com/o/Amazon%20Echo%20Dot2.jpg?alt=media&token=5aa98e84-5636-42a5-93f1-3131201b37c4"));
        banners.add(new RemoteBanner("https://i.ytimg.com/vi/iATzWl0HA_U/maxresdefault.jpg"));
        //add banner using resource drawable

        bannerSlider.setBanners(banners);


        //gridview.addHeaderView(headerView);


    }*/

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


    /*

    public class CustomAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder{

        private LayoutInflater layoutinflater;
        private List<ItemObject> listStorage;
        private Context context;


        public CustomAdapter(Context context, List<ItemObject> customizedListView) {
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

            convertView = layoutinflater.inflate(R.layout.listview_with_text_image, parent, false);

                listViewHolder = new ViewHolder(convertView);




            //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

            listViewHolder.textInListView.setText(listStorage.get(position).getContent());
            // int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(),null,null);
            //listViewHolder.imageInListView.setImageResource(imageResourceId);
            Glide.with(getActivity())
                    .load(listStorage.get(position).getImageResource())
                    .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .crossFade(200)
                    .into(listViewHolder.imageInListView);
            return convertView;
        }



    }
    */

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

        private LayoutInflater layoutinflater;
        Context context;
        private List<ItemObject> listStorage;
       // private ItemClickListener mClickListener;

        public RecyclerViewAdapter(Context context, List<ItemObject> customizedListView) {
            this.context = context;
            layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listStorage = customizedListView;
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView textInListView;
            ImageView imageInListView;
            public ViewHolder(View convertView){
                super(convertView);
                textInListView = (TextView)convertView.findViewById(R.id.textView);
                imageInListView = (ImageView)convertView.findViewById(R.id.gridimageView);
                convertView.setOnClickListener(this);




            }
            @Override
            public void onClick(View view) {
                onItemClick(view, getAdapterPosition());
            }

        }
        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

           // View view1 = LayoutInflater.from(context1).inflate(R.layout.r,parent,false);

           // ViewHolder viewHolder1 = new ViewHolder(view1);

            //return viewHolder1;
            View convertView = LayoutInflater.from(context).inflate(R.layout.listview_with_text_image, parent, false);

           ViewHolder listViewHolder = new ViewHolder(convertView);



            return listViewHolder;
            //final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);



        }

        @Override
        public void onBindViewHolder(ViewHolder listViewHolder, final int position){


            listViewHolder.textInListView.setText(listStorage.get(position).getContent());
            // int imageResourceId = this.context.getResources().getIdentifier(listStorage.get(position).getImageResource(),null,null);
            //listViewHolder.imageInListView.setImageResource(imageResourceId);
            Glide.with(getActivity())
                    .load(listStorage.get(position).getImageResource())
                    .thumbnail(Glide.with(getActivity()).load(R.drawable.spinner))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .crossFade(200)
                    .into(listViewHolder.imageInListView);
            listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(getActivity(), ProductDetailActivity.class);


                    myIntent.putExtra("image",allItems.get(position).getImageResource());
                    // Log.d("image2",allItems.get(position).getImageResource2());
                    myIntent.putExtra("image2",allItems.get(position).getImageResourcee());
                    myIntent.putExtra("details",allItems.get(position).getDetails());
                    myIntent.putExtra("cost",allItems.get(position).getCost());
                    myIntent.putExtra("text",allItems.get(position).getContent());
                    myIntent.putExtra("pos",position);
                    myIntent.putExtra("deadline",allItems.get(position).getDeadline());
                    startActivity(myIntent);

                }
            });


        }

        @Override
        public int getItemCount(){

            return listStorage.size();
        }
        public void onItemClick(View view, int position) {
            Log.i("TAG", "You clicked number " + listStorage.get(position).toString() + ", which is at cell position " + position);
        }
    }
}
