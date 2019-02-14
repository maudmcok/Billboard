package lms.mmm.mybillboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import lms.mmm.mybillboard.Model.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private User user;

    private DatabaseReference myUsers , mDatabase;

    // TODO: Rename and change types of parameters
    private String emailConnexion;

    //FOR DESIGN
    EditText textViewName;
    EditText textViewFirstName;
    TextView editTextEmail;
    EditText textViewPhone;
    EditText textViewAdresse;
    Button btnSave;
    Button btnClose;


    boolean result = false;

    private OnFragmentInteractionListener mListener;

    public ProfilFragment() {
        // Required empty public constructor
    }

            /**
     * Use this factory method to create a new instance ofà
     * this fragment using the provided parameters.
     *
     * @param emailConnexion Parameter 1.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String emailConnexion) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, emailConnexion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emailConnexion = getArguments().getString(ARG_PARAM1);
        }

        //Initialisation de mDatabase
        mDatabase  = FirebaseDatabase.getInstance().getReference();
        myUsers  = FirebaseDatabase.getInstance().getReference("users");

        if(isCurrentUserLogged())
        {
            this.getProfile(getCurrentUser().getEmail());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        textViewName = view.findViewById(R.id.tvName);
        textViewFirstName = view.findViewById(R.id.tvFirstName);
        editTextEmail = view.findViewById(R.id.etEmail);
        textViewPhone = view.findViewById(R.id.etPhone);
        textViewAdresse = view.findViewById(R.id.etAdresse);
        btnSave = view.findViewById(R.id.btnSave);
        btnClose = view.findViewById(R.id.btnClose);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result)
                {
                    updateProfile(emailConnexion);
                }
                else
                {
                    onClickSave();
                }
            }
        });

        btnClose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager manager = ((MainActivity) getContext()).getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        HomeFragment homeFragment = new HomeFragment();
                        transaction.replace(R.id.fragment_container,homeFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
        });
        return view ;
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

    @Nullable
    protected FirebaseUser getCurrentUser()
    {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged()
    {
        return (this.getCurrentUser() != null);
    }

    private void writeNewUser(String name, String firstName, String email, String adresse, String telephone) {
        String key = mDatabase.child("users").push().getKey();
        User user = new User(name,firstName,email,adresse,telephone);
        Map<String, Object> userValues = user.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + key, userValues);

        mDatabase.updateChildren(childUpdates).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Enregistrement réalisé",Toast.LENGTH_LONG).show();
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

    private void updateProfile(String str) {
        Query pendingTasks = myUsers.orderByChild("email").equalTo(str);
        pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    snapshot.getRef().child("name").setValue(textViewName.getText().toString());
                    snapshot.getRef().child("firstName").setValue(textViewFirstName.getText().toString());
                    snapshot.getRef().child("adresse").setValue(textViewAdresse.getText().toString());
                    snapshot.getRef().child("telephone").setValue(textViewPhone.getText().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase",""+databaseError);
            }
        });
    }

    public void getProfile(String str) {
            myUsers.orderByChild("email").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    user = childDataSnapshot.getValue(User.class);

                    textViewName.setText(user.getName());
                    textViewFirstName.setText(user.getFirstName());
                    editTextEmail.setText(user.getEmail());
                    textViewPhone.setText(user.getTelephone());
                    textViewAdresse.setText(user.getAdresse());

                    result = true;
                }

                if(dataSnapshot.getValue() == null){
                    result = false ;
                    editTextEmail.setText(emailConnexion);
                    //editTextEmail.setText("juste.test@gmail.com");
                }
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase",""+databaseError);
                System.out.println("tests" + databaseError);
                result = false;
            }} );

    }

    public void onClickSave() {
        String name = textViewName.getText().toString();
        String firstName = textViewFirstName.getText().toString();
        String adresse = textViewAdresse.getText().toString();
        String email = editTextEmail.getText().toString();
        String phone = textViewPhone.getText().toString();

        this.writeNewUser(name, firstName, email, adresse, phone);
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
}
