package com.example.yasir.nowchat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private android.support.v7.widget.Toolbar mToolbar;

    private ViewPager mViewPager;

    private PagerAdapterSection mViewPagerAdapter;

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("NowChat");

        mViewPager=(ViewPager)findViewById(R.id.main_viewPager);

        mViewPagerAdapter = new PagerAdapterSection(getSupportFragmentManager());

        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout= (TabLayout)findViewById(R.id.main_tabs);

        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user has not already signed in to the account
        // if he hasn't then go to StartActivity page directly

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser==null){
            Intent StartIntent = new Intent(MainActivity.this,StartActivity.class);
            startActivity(StartIntent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        if (item.getItemId()==R.id.main_logout_button){

            FirebaseAuth.getInstance().signOut();
            Intent startIntent = new Intent( MainActivity.this, StartActivity.class);

            startActivity(startIntent);

        }

        if(item.getItemId()==R.id.main_AccountSettings_Button){
            Intent settings_intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settings_intent);


        }

        if(item.getItemId()==R.id.main_AllUsers_Button){

            Intent Users_intent = new Intent(MainActivity.this,UsersActivity.class);
            startActivity(Users_intent);


        }
        return true;
    }
}
