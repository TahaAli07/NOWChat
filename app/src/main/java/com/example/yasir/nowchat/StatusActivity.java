package com.example.yasir.nowchat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private Button mButton;
    private TextInputLayout txt_input_layout;

    //firebase Code
    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mToolBar = findViewById(R.id.status_Toolbar);
        mButton = findViewById(R.id.status_Button);
        txt_input_layout = findViewById(R.id.status_textInputLayout);
        txt_input_layout.getEditText().setText(getIntent().getStringExtra("Status Value"));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mProgress = new ProgressDialog(StatusActivity.this);
                mProgress.setTitle("Saving Changes to DataBase");
                mProgress.setMessage("Please wait while we Save Changes");
                mProgress.show();

                String status = txt_input_layout.getEditText().getText().toString();

                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("USERS DATABASE").child(mCurrentUser.getUid());

                mDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            mProgress.dismiss();

                        } else {

                            Toast.makeText(StatusActivity.this, "Error in saving changes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
