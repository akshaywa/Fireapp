package com.example.walun.fireapp3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by walun on 18-07-2017.
 */

public class LoginActivity extends AppCompatActivity{
    private EditText aLoginEmailField;
    private EditText aLoginPasswordField;
    private Button aLoginBtn;
    private DatabaseReference ref;
    private String email,password;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);



        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<big>Login</big>"));



        mAuth=FirebaseAuth.getInstance();
        ref= FirebaseDatabase.getInstance().getReference().child("users");
        aLoginEmailField=(EditText) findViewById(R.id.loginEmailField);
        aLoginPasswordField=(EditText) findViewById(R.id.loginPasswordField);
        aLoginBtn=(Button) findViewById(R.id.loginBtn);
        mProgress=new ProgressDialog(this);

        aLoginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });
    }


    private void checkLogin(){
        email=aLoginEmailField.getText().toString().trim();
        password=aLoginPasswordField.getText().toString().trim();

        if(validate()==true){
            mProgress.setMessage("Signing In...");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserExist();
                    }
                    else{
                        mProgress.dismiss();
                        Toast.makeText(LoginActivity.this,"Error Login",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    public boolean validate() {
        boolean valid = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            aLoginEmailField.setError("Please Enter Valid Email Address");
            valid = false;
        }
        if (password.isEmpty()) {
            aLoginPasswordField.setError("Please Enter Valid Password");
            valid = false;
        }

        return valid;
    }

    private void checkUserExist(){
        final String user_id=mAuth.getCurrentUser().getUid();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)) {
                    mProgress.dismiss();

                    Intent loginIntent= new Intent(LoginActivity.this,SecondPage.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                    finish();

                }
                else{
                    Toast.makeText(LoginActivity.this,"You Need To Setup Your Account.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
