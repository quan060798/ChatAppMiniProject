package com.example.chatappminiproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatappminiproject.Moments;
import com.example.chatappminiproject.R;
import com.example.chatappminiproject.homepage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OwnMomentAdpater extends RecyclerView.Adapter<OwnMomentAdpater.OwnMomentViewHolder> {

    private Context Mcontext;
    private List<Moments> MmomentsList;

    public OwnMomentAdpater (Context context, List<Moments> moments)
    {
        Mcontext = context;
        MmomentsList = moments;
    }
    @NonNull
    @Override
    public OwnMomentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(Mcontext).inflate(R.layout.own_moment_display, parent, false);
        return new OwnMomentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnMomentViewHolder holder, int position) {
        Moments momentcurrent = MmomentsList.get(position);
        holder.description.setText(momentcurrent.getDescription());
        holder.time.setText(momentcurrent.getUploadtime());
        Picasso.with(Mcontext).load(momentcurrent.getMomentimageurl()).placeholder(R.mipmap.ic_launcher).fit().centerInside().into(holder.momentphoto);
    }

    @Override
    public int getItemCount() {
        return MmomentsList.size();
    }

    public class OwnMomentViewHolder extends RecyclerView.ViewHolder {
        public TextView description, time;
        public ImageView momentphoto;
        public OwnMomentViewHolder(@NonNull View itemView) {
            super(itemView);
            momentphoto = itemView.findViewById(R.id.moment_images);
            description = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.uploaddate);
        }
    }

}
