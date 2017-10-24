package com.example.walun.fireapp3;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by walun on 11-07-2017.
 */

public class SecondPage extends AppCompatActivity {

    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private RecyclerView mUsersList;
    private ArrayList<Blog> listContentArr= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondpage);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<big>Group List</big>"));

        mAuth = FirebaseAuth.getInstance();

        String user_id=mAuth.getCurrentUser().getUid();
        ref= FirebaseDatabase.getInstance().getReference().child("Groups").child(user_id);

        mUsersList=(RecyclerView)findViewById(R.id.my_recycler_view);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));


      }


    protected void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<Blog ,UsersViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, UsersViewHolder>(
                Blog.class,
                R.layout.blog_row,
                UsersViewHolder.class,
                ref

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Blog model, int position) {
                  final String post_key=getRef(position).getKey();


                   viewHolder.setNameGrp(model.getNameGrp());
                   viewHolder.setImageGrp(getApplicationContext(),model.getImageGrp());




                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleIntent=new Intent(SecondPage.this,SingleActivity.class);
                        singleIntent.putExtra("blog_id",post_key);
                        startActivity(singleIntent);

                    }
                });







            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
         static View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setNameGrp(String nameGrp){
            TextView userNameView=(TextView) mView.findViewById(R.id.username);
            userNameView.setText(nameGrp);
        }


        public void setImageGrp(Context ctx, String imageGrp){
            ImageView userImageView=(ImageView) mView.findViewById(R.id.picture);
            Picasso.with(ctx).load(imageGrp).into(userImageView);
        }

    }








    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.action_settings){
            startActivity(new Intent(SecondPage.this,FourthPage.class));
        }

        if(item.getItemId()==R.id.action_logout){
            logout();
        }

        if(item.getItemId()==R.id.action_next){
            startActivity(new Intent(SecondPage.this,ThirdPage.class));
        }

        if(item.getItemId()==R.id.enter_grp){
            startActivity(new Intent(SecondPage.this,SixthPage.class));
        }

        return super.onOptionsItemSelected(item);

    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(SecondPage.this,MainActivity.class));
        finish();
    }
}



