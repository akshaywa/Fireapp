package com.example.walun.fireapp3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPass;
    private Button mSubmit;
    private Button mLogin;
    private Button mFb;
    private DatabaseReference ref;
    private String name,email,password,pass;
    private ProgressDialog mProgress;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ref = FirebaseDatabase.getInstance().getReference().child("users");
        mName = (EditText) findViewById(R.id.editTextName);
        mEmail = (EditText) findViewById(R.id.editTextEmail);
        mPassword = (EditText) findViewById(R.id.editTextPassword);
        mPass = (EditText) findViewById(R.id.editTextPass);
        mSubmit = (Button) findViewById(R.id.buttonSubmit);
        mLogin = (Button) findViewById(R.id.buttonLogin);
        mFb = (Button) findViewById(R.id.buttonFb);
        mProgress=new ProgressDialog(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<big>Fireapp3</big>"));

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null){

                }else {

                    startActivity(new Intent(MainActivity.this, SecondPage.class));

                }
            }
        };

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                name=mName.getText().toString().trim();
                pass=mPass.getText().toString().trim();

                if(validate()==true){
                    mProgress.setMessage("Signing Up...");
                    mProgress.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Getting values to store
                                register();
                            } else {
                                Toast.makeText(MainActivity.this,"Sign up failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

        private void register() {
            initialize();
            if (!validate())
            {
                Toast.makeText(this, "Signup has Failed", Toast.LENGTH_SHORT).show();
            }
            else
            {
                onSignupSuccess();
            }
        }



        private void initialize() {
          name = mName.getText().toString().trim();
          email = mEmail.getText().toString().trim();
          password = mPassword.getText().toString().trim();
          pass = mPass.getText().toString().trim();
        }




    public boolean validate() {
        boolean valid = true;
        if (name.isEmpty() || name.length() > 32) {
            mName.setError("Please Enter Valid Name");
            valid = false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please Enter Valid Email Address");
            valid = false;
        }
        if (password.isEmpty() || password.length() < 8) {
            mPassword.setError("Please Enter Valid Password");
            valid = false;
        }
        if (pass.isEmpty() || !pass.equals(password)) {
            mPass.setError("Password Do Not Matches");
            valid = false;
        }

        return valid;
    }



    private void onSignupSuccess() {
        //Creating Person object
       // Person person = new Person();
        //Adding values
       // person.setName(name);
      //  person.setEmail(email);
        //person.setPassword(password);


        String user_id=mAuth.getCurrentUser().getUid();




        //Storing values to firebase
        //ref.child(user_id).setValue(person);

        ref.child(user_id).child("email").setValue(email);
        ref.child(user_id).child("name").setValue(name);
        ref.child(user_id).child("password").setValue(password);
        ref.child(user_id).child("profileimage").setValue("default");

        mProgress.dismiss();

        Intent mainIntent= new Intent(MainActivity.this,SecondPage.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);

    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            finish();
        }
    }







    public void onButtonClick(View v){
        if(v.getId()==R.id.buttonLogin)
        {
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
        }
    }


}
