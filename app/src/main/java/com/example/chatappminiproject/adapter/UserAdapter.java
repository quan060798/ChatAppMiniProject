package com.example.chatappminiproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatappminiproject.DisplayUser;
import com.example.chatappminiproject.DisplayUserProfile;
import com.example.chatappminiproject.Model.Users;
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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<Users> mUsers;
    private  boolean ischat;

    FirebaseUser firebaseUser;



    public UserAdapter(Context mContext, List<Users> mUsers, boolean isChat){
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat = isChat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Users users = mUsers.get(position);

        holder.btn_friend.setVisibility(View.VISIBLE);
        holder.username.setText(users.getUsername());

        if (users.getImageUrl()== null){
            holder.profile_image.setImageResource((R.mipmap.ic_launcher));
        }else{

           Picasso.get().load(users.getImageUrl()).placeholder(R.mipmap.ic_launcher).fit().centerInside().into(holder.profile_image);
        }

        isFriend(users.getId(), holder.btn_friend);

        if(users.getId().equals(firebaseUser.getUid())){
            holder.btn_friend.setVisibility(View.GONE);
        }

       if(ischat){
           if(String.valueOf(users.getStatus()).equals("Online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);

            }else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2userinfo = new Intent(mContext, DisplayUserProfile.class);
                intent2userinfo.putExtra("userid",users.getId());
                mContext.startActivity(intent2userinfo);
            }
        });

        holder.btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_friend.getText().toString().equals("Add Friend")){
                    FirebaseDatabase.getInstance().getReference().child("Friend").child(firebaseUser.getUid()).child("isFriend").child(users.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Friend").child(users.getId()).child("UserFriend").child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Friend").child(firebaseUser.getUid()).child("isFriend").child(users.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Friend").child(users.getId()).child("UserFriend").child(firebaseUser.getUid()).removeValue();

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        public Button btn_friend;
        private ImageView img_on, img_off;


        public  ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.tv_username);
            profile_image = itemView.findViewById(R.id.userprofile_image);
            btn_friend = itemView.findViewById(R.id.btn_friend);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
        }
    }

    private void isFriend(final String userid, final Button button){
        DatabaseReference references = FirebaseDatabase.getInstance().getReference().child("Friend").child(firebaseUser.getUid()).child("isFriend");
        references.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    button.setText("UnFriend");
                }else
                    button.setText("Add Friend");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
