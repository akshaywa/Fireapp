package com.example.walun.fireapp3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by walun on 20-08-2017.
 */

public class Adduser extends AppCompatActivity {
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private RecyclerView mUserList;
    private ArrayList<Bloguser> listContentArr= new ArrayList<>();
    private String keyy;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adduserpage);

        String keyy=getIntent().getExtras().getString("blog_id");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<big>Group Members</big>"));


        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("Groupmembers").child(keyy);


        mUserList=(RecyclerView)findViewById(R.id.my_recycler_viewee);
        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));
    }





    protected void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<Bloguser ,UsersViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Bloguser,UsersViewHolder>(
                Bloguser.class,
                R.layout.blog_rowuser,
                UsersViewHolder.class,
                ref

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Bloguser model, int position) {

                viewHolder.setNameuser(model.getList());
                viewHolder.setImageuser(getApplicationContext(),model.getListimg());

            }
        };
        mUserList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder{
        static View mViewe;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mViewe=itemView;
        }

        public void setNameuser(String list){
            TextView userNameView=(TextView) mViewe.findViewById(R.id.usernamee);
            userNameView.setText(list);
        }


        public void setImageuser(Context ctx, String listimg){
            ImageView userImageView=(ImageView) mViewe.findViewById(R.id.picturee);
            Picasso.with(ctx).load(listimg).into(userImageView);
        }

    }





























}


