package lms.mmm.mybillboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import lms.mmm.mybillboard.Model.Billboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private OnFragmentInteractionListener mListener;
    TextView code,type,prix,addresse ;
    public BuyFragment() {
    }

    private DatabaseReference myBillboards;
    private Billboard billboard ;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BuyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyFragment newInstance(String param1) {
        BuyFragment fragment = new BuyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        myBillboards  = FirebaseDatabase.getInstance().getReference("mybilboards");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);
        // Inflate the layout for this fragment
        code = view.findViewById(R.id.buyCode);
        prix = view.findViewById(R.id.buyPrix);
        type = view.findViewById(R.id.buyType);
        addresse = view.findViewById(R.id.buyAddresse);

        if (mParam1!= null)  getBillboard(mParam1) ; else  getBillboard("0");

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
                    type.setText(billboard.getType());
                    addresse.setText(billboard.getAddress());
                    prix.setText(""+billboard.getPrix());
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
