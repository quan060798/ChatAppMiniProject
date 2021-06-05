package com.example.chatappminiproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatappminiproject.MessageActivity;
import com.example.chatappminiproject.Model.Chat;
import com.example.chatappminiproject.Model.Users;
import com.example.chatappminiproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AllChatAdapter extends RecyclerView.Adapter<AllChatAdapter.ViewHolder> {

    private Context mcontext;
    private List<Users> mUsers;
    private  boolean ischat;
    String theLastMessage;

    public AllChatAdapter(Context mcontext, List<Users> mUsers,boolean ischat) {
        this.mcontext = mcontext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.useritemforchat, parent, false);

        return new AllChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = mUsers.get(position);
        holder.username.setText(users.getUsername());
        Glide.with(mcontext).load(users.getImageUrl()).into(holder.profile_image);

        if (ischat)
        {
            lastMessages(users.getId(), holder.lastmessage);
        }
        else {
            holder.lastmessage.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("userid", users.getId());
                mcontext.startActivity(intent);
            }
        });

        if(ischat){
            if(String.valueOf(users.getStatus()).equals("Online")){
                holder.img_On.setVisibility(View.VISIBLE);
                holder.img_Off.setVisibility(View.GONE);

            }else{
                holder.img_On.setVisibility(View.GONE);
                holder.img_Off.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_On.setVisibility(View.GONE);
            holder.img_Off.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        private TextView lastmessage;
        private ImageView img_On, img_Off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.usernamess);
            profile_image = itemView.findViewById(R.id.profile_imagess);
            lastmessage = itemView.findViewById(R.id.lastmessage);
            img_On = itemView.findViewById(R.id.img_On);
            img_Off = itemView.findViewById(R.id.img_Off);
        }
    }

    private void lastMessages(String userid, TextView lastmsg){
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid()))
                    {
                        theLastMessage = chat.getMessage();
                    }
                }

                switch (theLastMessage) {
                    case "default":
                        lastmsg.setText("No message");
                        break;
                    default:
                        lastmsg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
