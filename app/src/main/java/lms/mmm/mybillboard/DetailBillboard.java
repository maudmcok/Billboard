package lms.mmm.mybillboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import lms.mmm.mybillboard.Model.Billboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static androidx.core.content.ContextCompat.checkSelfPermission;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailBillboard.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailBillboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailBillboard extends Fragment {

    //view init
    TextView code,taill,prix,type,addresse,vue ;
    ImageView image ;
    Button mapButton;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private GoogleMap mMap;
    private DatabaseReference myBillboards;
    private Billboard billboard ;

    public DetailBillboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1
     * @return A new instance of fragment DetailBillboard.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailBillboard newInstance(String param1) {
        DetailBillboard fragment = new DetailBillboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myBillboards  = FirebaseDatabase.getInstance().getReference("mybilboards");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapView);

       // mapFragment.getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_billboard, container, false);

        code = view.findViewById(R.id.bCode);
        taill = view.findViewById(R.id.bTaille);
        prix = view.findViewById(R.id.bPrix);
        type = view.findViewById(R.id.bType);
        addresse = view.findViewById(R.id.bAddresse);
        vue = view.findViewById(R.id.bView);
        image = view.findViewById(R.id.bImage);
        mapButton = view.findViewById(R.id.mapView);

        if (mParam1!= null)  getBillboard(mParam1) ; else  getBillboard("0");

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((MainActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                //       DetailBillboard detailBillboard = new DetailBillboard();
                BuyFragment buyFragment = BuyFragment.newInstance(billboard.getCode());
                transaction.replace(R.id.fragment_container,buyFragment);
                transaction.addToBackStack(null);
                Log.v("fireBase","<> "+ billboard.getCode());
                transaction.commit();
               /* Snackbar.make(view, "Buy it", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), MapsActivity.class);
                myIntent.putExtra("billboard", billboard.getCode());
                myIntent.putExtra("lat", billboard.getLatitude());
                myIntent.putExtra("lg", billboard.getLongitude()); //Optional parameters
                getContext().startActivity(myIntent);
            }
        });

        return view;
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


    public void getBillboard(String bCode){
        myBillboards  = FirebaseDatabase.getInstance().getReference("mybilboards");
        myBillboards.orderByChild("code").equalTo(bCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d("Firebase", "PARENT: "+ childDataSnapshot.getKey());
                    Log.d("Firebase",""+ childDataSnapshot.toString());
                    billboard = childDataSnapshot.getValue(Billboard.class);
                    code.setText(billboard.getCode());
                    taill.setText(billboard.getTaille());
                    type.setText(billboard.getType());
                    addresse.setText(billboard.getAddress());
                    vue.setText(""+billboard.getVues());
                    Picasso.get().load(billboard.getImages().get(0)).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase",""+databaseError);
            }} );
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
