package lms.mmm.mybillboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lms.mmm.mybillboard.Adapter.MyAdapter;
import lms.mmm.mybillboard.DataBaseHelper.BillboardHelper;
import lms.mmm.mybillboard.Model.Billboard;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mDatabase, myBillboards;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    ArrayList<Billboard> list;
    MyAdapter adapter;
    BillboardHelper helper ;
    Button btMap;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Write a message to the database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myBillboards  = FirebaseDatabase.getInstance().getReference("mybilboards");



/*
        for (int i = 0 ; i< 10 ; i++) {
            ArrayList<String> images = new ArrayList<>() ;
            images.add("https://image.shutterstock.com/image-photo/blank-billboard-against-blue-sky-260nw-103397870.jpg");
            images.add("95542");
            images.add("xsxzx");
            ArrayList<Boolean> mois = new ArrayList<>() ;
            mois.add(true);
            mois.add(true);
            mois.add(false);
            mois.add(true);
            mois.add(true);
            mois.add(false);
            mois.add(true);
            mois.add(true);
            mois.add(false);
            mois.add(true);
            mois.add(true);
            mois.add(false);

            writeNewBillboard(" 263 Avenue Général Leclerc, 35000 Rennes",images,""+i,"dd","21","","","",true, mois,12);
        }
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ArrayList<Billboard> bs = new ArrayList<Billboard>();

        recyclerView = view.findViewById(R.id.billboardRecycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));

        adapter =new MyAdapter(getContext(),bs);
        helper = new BillboardHelper(myBillboards,adapter);

        btMap = view.findViewById(R.id.btBillMap);
        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(getContext(), MapsActivity.class);
                myIntent.putExtra("billboards", "aroundMe");
                getContext().startActivity(myIntent);
            }
        });


        recyclerView.setAdapter(adapter);

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
    private void writeNewBillboard(String address, List<String> images,
                                   String code, String Latitude ,String Longitude ,
                                   String prix, String taille,
                                   String type, Boolean disponible,
                                   List<Boolean> moisDisponible, int vues ) {

        String key = mDatabase.child("mybilboards").push().getKey();

        Billboard b = new Billboard(address, images,code, Latitude, Longitude, prix, taille, type, disponible, moisDisponible,vues  );

        Map<String, Object> postValues = b.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/mybilboards/" + key, postValues);
        mDatabase.updateChildren(childUpdates).
                addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),"ok push",Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
