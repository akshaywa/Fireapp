package com.example.walun.fireapp3;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by walun on 11-07-2017.
 */

public class ThirdPage extends AppCompatActivity {
    private EditText bGrpName;
    private EditText bGrpId;
    private EditText bGrpPass;
    private TextView bMemNumber;
    private ImageButton bNumUp;
    private ImageButton bNumDown;
    private ImageButton grpmage;
    private static final int GALLERY_REQUEST=1;
    private Button bDone;
    private FirebaseAuth mAuth;
    private DatabaseReference referi;
    private DatabaseReference referredi;
    private DatabaseReference refer;
    private DatabaseReference referred;
    private DatabaseReference refrr;
    private DatabaseReference refr;
    private StorageReference yStorage;
    private Uri zImageUri=null;
    private String namegrp,idgrp,passgrp,memgrp,downloadUrii,user_id,profileimage,name13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thirdpage);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<big>Create Group</big>"));

        referred = FirebaseDatabase.getInstance().getReference().child("Publicgrps");
        refer = FirebaseDatabase.getInstance().getReference().child("Groups");
        refrr=FirebaseDatabase.getInstance().getReference().child("Groupmembers");



        mAuth = FirebaseAuth.getInstance();
        user_id=mAuth.getCurrentUser().getUid();
        refr=FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        bGrpName=(EditText)findViewById(R.id.grpName);
        bGrpId=(EditText)findViewById(R.id.grpId);
        bGrpPass=(EditText)findViewById(R.id.grpPass);
        bMemNumber=(TextView) findViewById(R.id.memNumber);
        bNumUp=(ImageButton)findViewById(R.id.numUp);
        bNumDown=(ImageButton)findViewById(R.id.numDown);
        grpmage=(ImageButton) findViewById(R.id.grpmage);
        bDone=(Button)findViewById(R.id.done);
        yStorage= FirebaseStorage.getInstance().getReference();
        final int[] piit = {0};




        grpmage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
            }
        });


        refr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileimage=dataSnapshot.child("profileimage").getValue().toString();
                name13=dataSnapshot.child("name").getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








        bNumUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(piit[0] <100 && piit[0] >=0){
                        piit[0]++;
                        bMemNumber.setText(""+ piit[0]);
                    }

                }


            });

            bNumDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(piit[0] <=100 && piit[0] >0){
                        piit[0]--;
                        bMemNumber.setText(""+ piit[0]);
                    }

                }

            });

        
        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting values to store
                registergroup();
            }
       });    
    }

    private void registergroup() {
        initializegroup();
        if (validategroup()==false)
        {
            Toast.makeText(this, "Signup has Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            onCreateSuccess();
        }
    }

    private void initializegroup() {
        namegrp = bGrpName.getText().toString().trim();
        idgrp= bGrpId.getText().toString().trim();
        passgrp= bGrpPass.getText().toString().trim();
        memgrp= bMemNumber.getText().toString().trim();


    }

    public boolean validategroup() {
        boolean valid = true;
        if (namegrp.isEmpty()) {
            bGrpName.setError("Please Enter Valid Name");
            valid = false;
        }

        if (idgrp.isEmpty() || idgrp.length() < 8) {
            bGrpId.setError("Please Enter Valid 8 characters Id");
            valid = false;
        }

        if (passgrp.isEmpty() || passgrp.length() < 8) {
            bGrpPass.setError("Please Enter Valid 8 characters Password");
            valid = false;
        }

        if (memgrp.isEmpty()) {
            bGrpPass.setError("Please Enter Valid Members");
            valid = false;
        }

        return valid;
    }

    private void onCreateSuccess() {
        //Creating Person object
        Discussion discussion = new Discussion();

        //Adding values
        discussion.setNameGrp(namegrp);
        discussion.setIdGrp(idgrp);
        discussion.setPassGrp(passgrp);
        discussion.setMemGrp(memgrp);

        //Storing values to firebase
        String mGroupId = refer.child(user_id).push().getKey();

        referi = FirebaseDatabase.getInstance().getReference().child("Groups").child(user_id).child(mGroupId);
        referredi = FirebaseDatabase.getInstance().getReference().child("Publicgrps").child(mGroupId);

    if(zImageUri!=null) {
       StorageReference filepath = yStorage.child("groupphotos").child(zImageUri.getLastPathSegment());


       filepath.putFile(zImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               downloadUrii = taskSnapshot.getDownloadUrl().toString();
               Map<String, Object> hopperUpdates = new HashMap<String, Object>();
               hopperUpdates.put("imageGrp", downloadUrii);
               referi.updateChildren(hopperUpdates);
               referredi.updateChildren(hopperUpdates);
           }
       });
   }
        refer.child(user_id).child(mGroupId).setValue(discussion);
        referred.child(mGroupId).setValue(discussion);
        refrr.child(mGroupId).child(user_id).child("list").setValue(name13);
        refrr.child(mGroupId).child(user_id).child("listimg").setValue(profileimage);

        bGrpName.setText(null);
        bGrpId.setText(null);
        bGrpPass.setText(null);
        bMemNumber.setText(null);
        grpmage.setImageURI(null);

    }
    protected void onActivityResult(int requestcode,int resultcode,Intent data) {
        super.onActivityResult(requestcode, resultcode, data);

        if (requestcode == GALLERY_REQUEST && resultcode == RESULT_OK) {
            Uri imageUri=data.getData();
            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);
        }
        if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultcode == RESULT_OK) {
                zImageUri= result.getUri();
                grpmage.setImageURI(zImageUri);
            } else if (resultcode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}


