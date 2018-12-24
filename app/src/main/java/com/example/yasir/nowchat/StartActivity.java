package com.example.yasir.nowchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    //Start Activity has a button stating want to create new account which takes to the register activity

    public void OnStartToRegisterButtonClick(View view) {
        Intent register_intent = new Intent(StartActivity.this, RegisterActivity.class);
        startActivity(register_intent);


    }

    public void OnclickForLoginButton(View view) {

        Intent login_intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(login_intent);
    }
}
