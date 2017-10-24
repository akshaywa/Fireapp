package com.example.walun.fireapp3;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
 * Created by walun on 12-07-2017.
 */

public class FourthPage extends AppCompatActivity{
    private ImageButton ySetupImageBtn;
    private TextView textViewPersons;
    private ImageButton yeditted ;
    private Button ySetupSubmitBtn;
    private StorageReference yStorage;
    private static final int GALLERY_REQUEST=1;
    private DatabaseReference refr;
    private Uri yImageUri=null;


    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourthpage);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<big>Account Setup</big>"));


        mAuth = FirebaseAuth.getInstance();
        textViewPersons = (TextView) findViewById(R.id.textViewPersons);
        yeditted=(ImageButton) findViewById(R.id.image111);
        ySetupImageBtn=(ImageButton) findViewById(R.id.setupImageBtn);
        ySetupSubmitBtn=(Button) findViewById(R.id.setupSubmitBtn);
        yStorage= FirebaseStorage.getInstance().getReference();

        String user_id=mAuth.getCurrentUser().getUid();
        refr= FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        refr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               String profileimage=dataSnapshot.child("profileimage").getValue().toString();
               String name12=dataSnapshot.child("name").getValue().toString();
               String email12=dataSnapshot.child("email").getValue().toString();


                Picasso.with(FourthPage.this).load(profileimage).into(ySetupImageBtn);

                //Person person = dataSnapshot.getValue(Person.class);
                //String string = "Name: " + person.getName() + "\nEmail: " + person.getEmail() + "\n\n";
                String string = "Name: " + name12+ "\nEmail: " + email12 + "\n\n";
                textViewPersons.setText(string);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



       ySetupSubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startSetupAccount();
            }
        });

        ySetupImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });

        yeditted.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(FourthPage.this,FifthPage.class);
                startActivity(i);
            }
        });
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
                yImageUri= result.getUri();
                ySetupImageBtn.setImageURI(yImageUri);
            } else if (resultcode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void startSetupAccount(){
        final String user_id=mAuth.getCurrentUser().getUid();

        if(yImageUri!= null){
            StorageReference filepath=yStorage.child("profilephotos").child(yImageUri.getLastPathSegment());

            filepath.putFile(yImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUri=taskSnapshot.getDownloadUrl().toString();
                    Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                    hopperUpdates.put("profileimage", downloadUri);
                    refr.updateChildren(hopperUpdates);
                }
            });

        }

    }








}

















