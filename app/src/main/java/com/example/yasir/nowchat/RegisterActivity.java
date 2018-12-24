package com.example.yasir.nowchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mDisplayEmail;
    private TextInputLayout mDisplayPassword;
    private Button OnaccCreateButton;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;

    //firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDBrefernce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolbar = findViewById(R.id.reg_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);

        mDisplayName = findViewById(R.id.reg_name);
        mDisplayEmail = findViewById(R.id.reg_email);
        mDisplayPassword = findViewById(R.id.reg_password);
        OnaccCreateButton = findViewById(R.id.reg_createAccount_button);

        //firebase
        mAuth = FirebaseAuth.getInstance();


        OnaccCreateButton.setOnClickListener(new View.OnClickListener() {
            //On click listener for create new account button
            @Override
            public void onClick(View view) {

                String name = mDisplayName.getEditText().getText().toString();
                String email = mDisplayEmail.getEditText().getText().toString();
                String password = mDisplayPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                    mProgressDialog.setMessage("Please Wait while you are registered");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    register_user(name, email, password);
                }
            }
        });

    }

    //firebase code to register new user
    private void register_user(final String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            mDBrefernce = FirebaseDatabase.getInstance().getReference().child("USERS DATABASE").child(uid);

                            HashMap<String, String> user_map = new HashMap<>();
                            user_map.put("name", name);
                            user_map.put("status", "Hey There ! I am Using NowChat");
                            user_map.put("image", "default");
                            user_map.put("thumb_image", "default");

                            mDBrefernce.setValue(user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mProgressDialog.dismiss();
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                        finish();

                                    }

                                }
                            });

                        } else {
                            mProgressDialog.hide();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }


                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
