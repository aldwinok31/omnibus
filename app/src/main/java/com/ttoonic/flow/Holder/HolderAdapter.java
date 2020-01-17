package com.ttoonic.flow.Holder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ttoonic.flow.DatabaseInit;
import com.ttoonic.flow.Interface.DatabaseInteractive;
import com.ttoonic.flow.Interface.RecyclerInteractive;
import com.ttoonic.flow.Model.Fault;
import com.ttoonic.flow.Model.User;
import com.ttoonic.flow.R;

import java.util.ArrayList;


public class HolderAdapter extends RecyclerView.Adapter<Holder>  {

    private Context context;
    private ArrayList<Fault> faults;
    private View view;
    private RecyclerInteractive recyclerInteractive;
    private String name;
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         this.view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.layout_dom_recycler,parent,false);
         return new Holder(view);

    }



    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        if (faults.isEmpty()){ return;}
        try {
            holder.creator.setText(faults.get(position).getCreator());
            holder.title.setText(faults.get(position).getTitle());
            holder.desc.setText(faults.get(position).getDescription());
            holder.timestamp.setText(faults.get(position).getTimestamp().toString());
            holder.group.setText(faults.get(position).getCategory());




            switch (faults.get(position).getType()) {
                case "Fire":
                    holder.type.setText(faults.get(position).getType());
                    holder.type.setBackgroundColor(Color.RED);
                    break;
                case "Flood":
                    holder.type.setText(faults.get(position).getType());
                    holder.type.setBackgroundColor(Color.BLUE);
                    break;
                case "Quake":
                    holder.type.setText(faults.get(position).getType());
                    holder.type.setBackgroundColor(Color.BLACK);
                    break;
                default:
                    holder.type.setText(faults.get(position).getType());
                    holder.type.setBackgroundColor(Color.GRAY);
                    break;
            }

            if(faults.get(position).getMarked_safe().contains(name)){
                holder.safe_text.setText(Integer.toString(faults.get(position).getMarked_safe().size() ) + " ,You");
                if(faults.get(position).getMarked_unsafe().isEmpty()){
                 holder.unsafe.setVisibility(View.GONE);
              }
                else {
                    holder.unsafe_text.setText(Integer.toString(faults.get(position).getMarked_unsafe().size()));
                }
            }
            else if (faults.get(position).getMarked_unsafe().contains(name)){
                holder.unsafe_text.setText(Integer.toString(faults.get(position).getMarked_unsafe().size() ) + " ,You");
                if(faults.get(position).getMarked_safe().isEmpty()){
                    holder.safe.setVisibility(View.GONE);
                }else {
                    holder.safe_text.setText(Integer.toString(faults.get(position).getMarked_safe().size()));
                }
            }

            Picasso.get().load(faults.get(position).getImgpath()).fit().into(holder.image);

            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(faults.get(position).getCreator(),faults.get(position));
                }
            });
        }
        catch (NullPointerException n){
            n.printStackTrace();
        }
    }

    public void showDialog(final String title, final Fault fault){

        DatabaseInit databaseInit = new DatabaseInit();
        databaseInit.addDatabaseSuccessListener(new DatabaseInteractive() {
            @Override
            public void onDatabaseSuccess(boolean data, Object object, String message) {
               if(object instanceof User) {
                   AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
                           .setIcon(R.drawable.mysafety)
                           .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                               }
                           })
                           .setPositiveButton("View Location", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   if(recyclerInteractive != null){
                                       recyclerInteractive.onRecycleInteract(fault);
                                   }
                                   dialog.dismiss();
                               }
                           })
                           .setMessage("Phone number: "+((User) object).getPhone_number())
                           .setTitle(((User) object).getUsername());
                   alert.create();
                   alert.show();
               }
            }

        });

        databaseInit.get_user(title);


    }

    public HolderAdapter(Context context, ArrayList<Fault> faults,RecyclerInteractive recyclerInteractive,String name) {
        this.context = context;
        this.faults = faults;
        this.recyclerInteractive = recyclerInteractive;
        this.name = name;

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
