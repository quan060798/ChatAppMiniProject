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
import com.example.chatappminiproject.Model.Users;
import com.example.chatappminiproject.Moments;
import com.example.chatappminiproject.R;
import com.example.chatappminiproject.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OtherMomentAdapter extends RecyclerView.Adapter<OtherMomentAdapter.ViewHolder> {

    public Context mContext;
    public List<Moments> momentsList;

    private FirebaseUser firebaseUser;

    public OtherMomentAdapter(Context mContext, List<Moments> momentsList) {
        this.mContext = mContext;
        this.momentsList = momentsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.other_moment_item, parent, false);
        return new OtherMomentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Moments moments = momentsList.get(position);
        Glide.with(mContext).load(moments.getMomentimageurl()).into(holder.momentimage);

        if (moments.getDescription().equals(""))
        {
            holder.description.setVisibility(View.GONE);
        }
        else
        {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(moments.getDescription());
        }
        holder.date.setText(moments.getUploadtime());
        uploaderInfo(holder.imageprofile, holder.username, holder.uploader, moments.getPoster());

    }

    @Override
    public int getItemCount() {
        return momentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageprofile, momentimage, like;
        public TextView username, likes, uploader, description, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageprofile = itemView.findViewById(R.id.image_profile);
            momentimage = itemView.findViewById(R.id.other_moment_image);
            like = itemView.findViewById(R.id.like);
            username = itemView.findViewById(R.id.username);
            likes = itemView.findViewById(R.id.like_number);
            uploader = itemView.findViewById(R.id.uploader);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.upload_date);

        }
    }

    private void uploaderInfo (final ImageView imageprofile, final TextView username, TextView uploader, final String userid)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Picasso.get().load(users.getImageUrl()).placeholder(R.mipmap.ic_launcher).fit().centerInside().into(imageprofile);
                username.setText(users.getUsername());
                uploader.setText(users.getUsername());
                System.out.println("Testing on othermomentadapter");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
