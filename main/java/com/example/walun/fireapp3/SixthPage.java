package com.example.walun.fireapp3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by walun on 20-08-2017.
 */

public class SixthPage extends AppCompatActivity{

    private EditText entgrpid;
    private EditText entgrppass;
    private Button entbtn;
    private DatabaseReference refr;
    private DatabaseReference referred;
    private DatabaseReference refrr;
    private DatabaseReference refer;
    private FirebaseAuth mAuth;
    private String user_id,grpid,grppass,alia,pass12,img12,mem12,name12,id12,profileimage,name13;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sixthpage);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<big>Enter Group</big>"));

        mAuth = FirebaseAuth.getInstance();
        user_id=mAuth.getCurrentUser().getUid();
        refer=FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        refr= FirebaseDatabase.getInstance().getReference().child("Publicgrps");
        referred = FirebaseDatabase.getInstance().getReference().child("Groups").child(user_id);
        refrr=FirebaseDatabase.getInstance().getReference().child("Groupmembers");


        entgrpid=(EditText)findViewById(R.id.entgrpid);
        entgrppass=(EditText)findViewById(R.id.entgrppass);
        entbtn=(Button)findViewById(R.id.entbtn);

        entbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grpid=entgrpid.getText().toString().trim();
                grppass=entgrppass.getText().toString().trim();

                if (validate()==false)
                {
                    Toast.makeText(SixthPage.this,"No Group Found",Toast.LENGTH_LONG).show();
                }
                else{
                    onCreateSuccess();
                }
            }
        });


        refer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profileimage=dataSnapshot.child("profileimage").getValue().toString();
                name13=dataSnapshot.child("name").getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        if (grpid.isEmpty() || grpid.length() <8) {
            entgrpid.setError("Please Enter Valid Name");
            valid = false;
        }
        if (grppass.isEmpty() || grppass.length() < 8) {
            entgrppass.setError("Please Enter Valid Password");
            valid = false;
        }
        return valid;
    }

    private void onCreateSuccess() {

        refr.orderByChild("idGrp").equalTo(grpid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object obj = null;
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    obj = objSnapshot.getKey();
                }
                alia= (String) obj;
                pass12=dataSnapshot.child(alia).child("passGrp").getValue().toString();
                img12=dataSnapshot.child(alia).child("imageGrp").getValue().toString();
                name12=dataSnapshot.child(alia).child("nameGrp").getValue().toString();
                mem12=dataSnapshot.child(alia).child("memGrp").getValue().toString();
                id12=dataSnapshot.child(alia).child("idGrp").getValue().toString();

                if (pass12.equals(grppass)) {
                    referred.child(alia).child("idGrp").setValue(id12);
                    referred.child(alia).child("imageGrp").setValue(img12);
                    referred.child(alia).child("passGrp").setValue(pass12);
                    referred.child(alia).child("nameGrp").setValue(name12);
                    referred.child(alia).child("memGrp").setValue(mem12);

                    refrr.child(alia).child(user_id).child("list").setValue(name13);
                    refrr.child(alia).child(user_id).child("listimg").setValue(profileimage);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User",databaseError.getMessage() );
            }
        });

        entgrpid.setText(null);
        entgrppass.setText(null);
    }

}
