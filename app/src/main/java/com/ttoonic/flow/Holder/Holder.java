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
    TextView timestamp;
    TextView group;
    TextView type;
    View view;
    View safe;
    View unsafe;
    TextView safe_text;
    TextView unsafe_text;


    public Holder(@NonNull View itemView) {
        super(itemView);

        this.type = itemView.findViewById(R.id.type_recycler);
        this.image = itemView.findViewById(R.id.image_recycler);
        this.desc = itemView.findViewById(R.id.description_recycler);
        this.title = itemView.findViewById(R.id.title_recycler);
        this.creator = itemView.findViewById(R.id.creator_recycler);
        this.timestamp = itemView.findViewById(R.id.timestamp_recycler);
        this.group = itemView.findViewById(R.id.group_recycler);
        this.view = itemView.findViewById(R.id.holder_recycler);
        this.safe = itemView.findViewById(R.id.safe_holder);
        this.unsafe = itemView.findViewById(R.id.unsafe_holder);
        this.safe_text = itemView.findViewById(R.id.marked_safe);
        this.unsafe_text = itemView.findViewById(R.id.marked_unsafed);

    }

}
