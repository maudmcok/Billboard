package lms.mmm.mybillboard.DataBaseHelper;

import android.os.Debug;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import lms.mmm.mybillboard.Adapter.MyAdapter;
import lms.mmm.mybillboard.Model.Billboard;

public class BillboardHelper {

        DatabaseReference db;
        Boolean saved=null;
    MyAdapter adapter ;

    public BillboardHelper(DatabaseReference db, final MyAdapter adapter) {
        this.db = db;
        this.adapter = adapter ;
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

        //SAVE
        public Boolean save(Billboard b)
        {
            if(b==null)
            {
                saved=false;
            }else {

                try
                {
                    String key = db.child("mybilboards").push().getKey();
                    db.child("/mybilboards/"+key).push().setValue(b);
                    saved=true;
                }catch (DatabaseException e)
                {
                    e.printStackTrace();
                    saved=false;
                }

            }

            return saved;
        }

        //READ
    /*    public ArrayList<Billboard> retrieve()
    {
        Log.e("firebase size", billboards.size()+"");
        return billboards;
    }*/

        private void fetchData(DataSnapshot dataSnapshot)
        {
           // billboards.clear();

            if (dataSnapshot.getValue(Billboard.class) != null){
                Billboard b  = dataSnapshot.getValue(Billboard.class);
               adapter.getBillboards().add(b);
            }

        }

    }