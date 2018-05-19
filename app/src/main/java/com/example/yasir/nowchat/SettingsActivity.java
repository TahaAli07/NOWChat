package com.example.yasir.nowchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import org.w3c.dom.Text;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView mImage;
    private TextView mname;
    private TextView mStatus;
    private Button mStatusChangeButton;
    private Button mImageButton;

    private static final int PICK_IMAGE = 1;

    private DatabaseReference mDatabase;
    private FirebaseUser mCurrentUser;

    //Storage
    private StorageReference mStorageRef;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mname = findViewById(R.id.settings_name_textView);
        mStatus = findViewById(R.id.settings_status_textView);
        mStatusChangeButton = findViewById(R.id.settings_ChangeStatus_Button);
        mImageButton = findViewById(R.id.settings_ChangeImage_Button);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mImage = findViewById(R.id.circleImageView);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("USERS DATABASE").child(mCurrentUser.getUid());
        
        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mname.setText(name);
                mStatus.setText(status);

                Toast.makeText(SettingsActivity.this, image, Toast.LENGTH_SHORT).show();

                Picasso.get().load(image).into(mImage);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        mStatusChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent status_activity = new Intent(SettingsActivity.this, StatusActivity.class);
                status_activity.putExtra("Status Value", mStatus.getText().toString());

                startActivity(status_activity);

            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialog= new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle("Uploading Image ...");
                mProgressDialog.setMessage("Please wait while we upload the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                String Current_user_id = mCurrentUser.getUid();
                StorageReference filepath = mStorageRef.child("profile pictures").child(Current_user_id+".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            String ImageUrl = task.getResult().getDownloadUrl().toString();

                            mDatabase.child("image").setValue(ImageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(SettingsActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                                    }
                                    else{

                                        Toast.makeText(SettingsActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });


                        }
                        else{
                            Toast.makeText(SettingsActivity.this, "ERROR IN UPLOADING", Toast.LENGTH_SHORT).show();
                        }
                        mProgressDialog.dismiss();
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

}
