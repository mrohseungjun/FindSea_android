package com.fndsea.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Fishes> fishList;

    public RecyclerAdapter(List<Fishes> fishList) {
        this.fishList = fishList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tableYear, wti, wtemp, temp, atm, Catch, ws;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tableYear = (TextView) itemView.findViewById(R.id.tableYear);
            wti = (TextView) itemView.findViewById(R.id.wti);
            wtemp = (TextView) itemView.findViewById(R.id.wtemp);
            temp = (TextView) itemView.findViewById(R.id.temp);
            atm = (TextView) itemView.findViewById(R.id.atm);
            ws = (TextView) itemView.findViewById(R.id.ws);
            Catch = (TextView) itemView.findViewById(R.id.Catch);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_adapter, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tableYear.setText(position + 1 + "일");
        holder.wti.setText(String.format("%.2f", fishList.get(position).getWti()));
        holder.wtemp.setText(String.format("%.2f", fishList.get(position).getwTmp()));
        holder.temp.setText(String.format("%.2f", fishList.get(position).getTmp()));
        holder.atm.setText(String.format("%.2f", fishList.get(position).getAtm()));
        holder.ws.setText(String.format("%.2f", fishList.get(position).getWs()));
        holder.Catch.setText(String.valueOf((int) fishList.get(position).getCatchVal()));
    }

    @Override
    public int getItemCount() {
        return fishList.size();
    }
}
