package com.ttoonic.flow.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ttoonic.flow.R;

public class Holder  extends RecyclerView.ViewHolder {

    ImageView image;
     TextView desc;
     TextView creator;
    TextView title;

    public Holder(@NonNull View itemView) {
        super(itemView);

        this.image = itemView.findViewById(R.id.image_recycler);
        this.desc = itemView.findViewById(R.id.description_recycler);
        this.title = itemView.findViewById(R.id.title_recycler);
        this.creator = itemView.findViewById(R.id.creator_recycler);

    }

}
