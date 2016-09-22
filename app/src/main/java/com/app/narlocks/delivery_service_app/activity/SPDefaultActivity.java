package com.app.narlocks.delivery_service_app.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.narlocks.delivery_service_app.extras.Image;
import com.app.narlocks.delivery_service_app.session.SessionManager;

public class SPDefaultActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_default);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_sp_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_sp_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new TesteFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, fragment).commit();

        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_sp_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.default_sp, menu);

        SessionManager session = new SessionManager(getApplicationContext());

        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvUserEmail = (TextView) findViewById(R.id.tvUserEmail);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvUserName.setText(session.getUserName());
        tvUserEmail.setText(session.getUserEmail());
        ivProfileImage.setImageBitmap(Image.base64ToBitmap(session.getUserProfileImage()));

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ClientDetailsFragment();

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.content_default_sp, fragment).commit();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_sp_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.navMyProjects) {
            fragment = new ClientProjectsFragment();
        } else if (id == R.id.navEvaluation) {
            fragment = new SPEvaluationsFragment();
        } else if (id == R.id.navExit) {
            SessionManager session = new SessionManager(this);
            session.logoutUser();
            finish();

            return true;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_default_sp, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_sp_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
