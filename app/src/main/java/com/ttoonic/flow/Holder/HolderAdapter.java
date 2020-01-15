package com.ttoonic.flow.Holder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ttoonic.flow.DatabaseInit;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Model.Fault;
import com.ttoonic.flow.R;

import java.util.ArrayList;

public class HolderAdapter extends RecyclerView.Adapter<Holder>  {

    private Context context;
    private ArrayList<Fault> faults;
    private View view;
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         this.view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.layout_dom_recycler,parent,false);
         return new Holder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        holder.creator.setText(faults.get(position).getCreator());
        holder.title.setText(faults.get(position).getTitle());
        holder.desc.setText(faults.get(position).getDescription());
        DatabaseInit databaseInit = new DatabaseInit();
        databaseInit.addDatabaseSuccessListener(new DatabaseInteractive() {
            @Override
            public void onDatabaseSuccess(boolean data, final Object object, String message) {
                Picasso.get().load(object.toString()).fit().into(holder.image);
            }
        });
        databaseInit.get_path(faults.get(position).getImgpath());

    }

    public HolderAdapter(Context context, ArrayList<Fault> faults) {
        this.context = context;
        this.faults = faults;
    }


    @Override
    public int getItemCount() {
        return faults.size();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull Holder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



}
