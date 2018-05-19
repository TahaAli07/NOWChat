package com.example.yasir.nowchat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private DatabaseReference mUsersdatabase;

    private CircleImageView mCircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar=findViewById(R.id.users_toolbar);
        mCircleImageView = findViewById(R.id.users_circleImageView);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersdatabase= FirebaseDatabase.getInstance().getReference().child("USERS DATABASE");

        mRecyclerView=findViewById(R.id.users_RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance().getReference().child("USERS DATABASE");

        FirebaseRecyclerOptions<Users> options;
        options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(query,Users.class)
                .build();


        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(options) {

            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item


                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.users_single_layout, parent,false);


                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UsersViewHolder holder, int position, Users model) {


                holder.setname(model.getName());
                holder.setstatus(model.getStatus());
                holder.setimage(model.getImage());



            }
        };

        adapter.startListening();

        mRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public  class UsersViewHolder extends  RecyclerView.ViewHolder{

         View mView;

         public UsersViewHolder(View itemView) {
            super(itemView);

            mView=itemView;
        }

        public void setname(String name){

            TextView UsernameView = mView.findViewById(R.id.users_displayname_textView);
            UsernameView.setText(name);

        }

        public void setstatus(String status){

            TextView StatusView = mView.findViewById(R.id.users_status_textView);
            StatusView.setText(status);

        }

        public void setimage(String image){


            Toast.makeText(UsersActivity.this, image, Toast.LENGTH_LONG).show();

            Picasso.get().load(image).into(mCircleImageView);


        }
    }
}
