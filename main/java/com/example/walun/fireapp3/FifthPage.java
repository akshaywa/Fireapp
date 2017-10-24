package com.example.walun.fireapp3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by walun on 01-08-2017.
 */

public class FifthPage extends AppCompatActivity {
    private EditText mkName;
    private EditText mkEmail;
    private Button edited;
    private DatabaseReference refr;
    private FirebaseAuth mAuth;
    private String name2,email2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fifthpage);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<big>Edit Details</big>"));

        mAuth = FirebaseAuth.getInstance();
        mkName = (EditText) findViewById(R.id.editName);
        mkEmail = (EditText) findViewById(R.id.editEmail);
        edited = (Button) findViewById(R.id.editedall);

        String user_id=mAuth.getCurrentUser().getUid();
        refr= FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        edited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name2 = mkName.getText().toString().trim();
                email2 = mkEmail.getText().toString().trim();

                if(validate()==true) {
                    Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                    hopperUpdates.put("name", name2);
                    hopperUpdates.put("email", email2);
                    refr.updateChildren(hopperUpdates);

                    mkName.setText(null);
                    mkEmail.setText(null);
                }
            }
        });
    }


    public boolean validate() {
        boolean valid = true;
        if (name2.isEmpty() || name2.length() > 32) {
            mkName.setError("Please Enter Valid Name");
            valid = false;
        }
        if (email2.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email2).matches()) {
            mkEmail.setError("Please Enter Valid Email Address");
            valid = false;
        }
        return valid;
    }
}
