package com.example.yasir.nowchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mButton;

    //firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        mProgressDialog =new ProgressDialog(this);

        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmail=findViewById(R.id.login_email);
        mPassword=findViewById(R.id.login_password);
        mButton=findViewById(R.id.login_loginButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

                    mProgressDialog.setMessage("Logging in");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    login_user(email,password);
                }

            }
        });

    }

    private void login_user(String email , String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            mProgressDialog.dismiss();
                            Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();


                        } else {
                            mProgressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


    }});
}
    }




