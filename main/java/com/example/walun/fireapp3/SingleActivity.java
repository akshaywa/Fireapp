package com.example.walun.fireapp3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by walun on 19-08-2017.
 */

public class SingleActivity extends AppCompatActivity {

    private DatabaseReference refr;
    private DatabaseReference refrr;
    private DatabaseReference reference;
    private DatabaseReference referred;
    private StorageReference yStorage;
    private FirebaseAuth mAuth;
    private ImageView profimage;
    private ImageButton emojibtn;
    private ImageButton sendbtn;
    private static final int GALLERY_REQUEST=1;
    private static String TAG;
    EmojiconEditText emojiconEditText;
    EmojiconTextView textView;
    View rootView;
    EmojIconActions emojIcon;
    private String post_key, strDate, user_id, name12,imagry,kzImageUri=null,downloadUrii;

    private RecyclerView mMessagesList;
    private ArrayList<Messages> listContentArr = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singlepage);

        post_key = getIntent().getExtras().getString("blog_id");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        strDate = "Current Time : " + mdformat.format(calendar.getTime());

        //Toast.makeText(SingleActivity.this,post_key,Toast.LENGTH_LONG).show();

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        profimage = (ImageView) findViewById(R.id.profimgbtn);
        emojibtn = (ImageButton) findViewById(R.id.emojibtn);
        sendbtn = (ImageButton) findViewById(R.id.sendbtn);
        refr = FirebaseDatabase.getInstance().getReference().child("Groups").child(user_id).child(post_key);
        refrr = FirebaseDatabase.getInstance().getReference().child("Chat");
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        referred = FirebaseDatabase.getInstance().getReference().child("Chat").child(post_key);
        yStorage= FirebaseStorage.getInstance().getReference();

        emojiconEditText = (EmojiconEditText) findViewById(R.id.writemsg);
        rootView = findViewById(R.id.root_view);


        mMessagesList=(RecyclerView)findViewById(R.id.message_list);
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(new LinearLayoutManager(this));





        emojIcon = new EmojIconActions(this, rootView, emojiconEditText, emojibtn);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e(TAG, "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e(TAG, "Keyboard closed");
            }
        });


        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = emojiconEditText.getText().toString();
                textView.setText(newText);
            }
        });


        refr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name12 = dataSnapshot.child("nameGrp").getValue().toString();
                String profileimage = dataSnapshot.child("imageGrp").getValue().toString();

                Picasso.with(SingleActivity.this).load(profileimage).into(profimage);

                Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                setSupportActionBar(myToolbar);
                getSupportActionBar().setTitle(name12);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                myToolbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleIntent = new Intent(SingleActivity.this, Adduser.class);
                        singleIntent.putExtra("blog_id", post_key);
                        startActivity(singleIntent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name12 = dataSnapshot.child("name").getValue().toString();
                imagry = dataSnapshot.child("profileimage").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }







    private void sendMessage() {
        String msg = emojiconEditText.getText().toString();

        if (!TextUtils.isEmpty(msg)) {
            String gGroupId = refrr.child(post_key).push().getKey();
            refrr.child(post_key).child(gGroupId).child("message").setValue(msg);
            refrr.child(post_key).child(gGroupId).child("time").setValue(strDate);
            refrr.child(post_key).child(gGroupId).child("sender").setValue(user_id);
            refrr.child(post_key).child(gGroupId).child("sendername").setValue(name12);
            refrr.child(post_key).child(gGroupId).child("imagery").setValue(imagry);
        }
        emojiconEditText.setText(null);
    }



    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Messages, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Messages, UsersViewHolder>(
                Messages.class,
                R.layout.message_single_layout,
                UsersViewHolder.class,
                referred

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Messages model, int position) {
                viewHolder.setMessageee(model.getMessage());
                viewHolder.setSendernameee(getApplicationContext(), model.getSendername());
                viewHolder.setTimeee(getApplicationContext(), model.getTime());
                viewHolder.setImagee(getApplicationContext(),model.getImagery());
            }
        };
        mMessagesList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        static View mViewee;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mViewee = itemView;
        }

        public void setMessageee(String message) {
            TextView userMsgView = (TextView) mViewee.findViewById(R.id.msmessage);
            userMsgView.setText(message);
        }

        public void setImagee(Context ctx, String image) {
            ImageView imageviewery=(ImageView) mViewee.findViewById(R.id.imageView3);
            Picasso.with(ctx).load(image).into(imageviewery);
        }


        public void setSendernameee(Context ctx, String sendername) {
            TextView userNameView = (TextView) mViewee.findViewById(R.id.msname);
            userNameView.setText(sendername);
        }

        public void setTimeee(Context ctx, String time) {
            TextView userTimeView = (TextView) mViewee.findViewById(R.id.mstime);
            userTimeView.setText(time);
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secondmain, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String kgGroupId = refrr.child(post_key).push().getKey();

        if (item.getItemId() == R.id.pdfs) {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }







}

