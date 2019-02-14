package lms.mmm.mybillboard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import lms.mmm.mybillboard.DetailBillboard;
import lms.mmm.mybillboard.MainActivity;
import lms.mmm.mybillboard.Model.Billboard;
import lms.mmm.mybillboard.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Billboard> billboards;

    public ArrayList<Billboard> getBillboards() {
        return billboards;
    }

    public void setBillboards(ArrayList<Billboard> billboards) {
        this.billboards = billboards;
    }

    public MyAdapter(Context c , ArrayList<Billboard> b)
    {
        context = c;
        billboards = b;

        Log.d("firebase **",billboards+"");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("firebase **",billboards.get(position).getImages().get(0)+"");
        holder.vue.setText(""+billboards.get(position).getVues());
        holder.addresse.setText(""+billboards.get(position).getAddress());
        Picasso.get().load(billboards.get(position).getImages().get(0).toString()).into(holder.billPic);
        if(billboards.get(position).getDisponible()) {
            holder.btn.setVisibility(View.VISIBLE);
            holder.onClick(position);
        }

    }

    @Override
    public int getItemCount() {
        return billboards.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView vue;
        TextView addresse;
        ImageView billPic;
        Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            vue = itemView.findViewById(R.id.vue_bill);
            addresse = itemView.findViewById(R.id.addesse_bill);
            billPic =  itemView.findViewById(R.id.image_bill);
            btn =  itemView.findViewById(R.id.button_bill);

        }
        public void onClick(final int position)
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    DetailBillboard detailBillboard = DetailBillboard.newInstance(billboards.get(position).getCode());
                    transaction.replace(R.id.fragment_container,detailBillboard);
                    transaction.addToBackStack(null);
                    Log.v("fireBase","<> "+ billboards.get(position).toMap());
                    transaction.commit();
                }
            });
        }
    }
}