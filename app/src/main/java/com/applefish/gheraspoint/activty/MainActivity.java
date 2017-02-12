package com.applefish.gheraspoint.activty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.applefish.gheraspoint.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String registertype;
    //keys
    private static final String LOGIN_KEY= "com.applefish.gheraspoint.LOGIN_KEY";
    //getString
    private String LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LOGIN=getBaseContext().getString(R.string.login);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        registertype=readSharedPreference(LOGIN_KEY,LOGIN);
        // get menu from navigationView
        Menu menu = navigationView.getMenu();
        if(registertype.equals("visitor"))
        {

            MenuItem task = menu.findItem(R.id.nav_task);
            task.setVisible(false);
            MenuItem loginout = menu.findItem(R.id.nav_loginout);
            loginout.setTitle("تسجيل دخول");
        }
        else
        {
            MenuItem item = menu.findItem(R.id.nav_task);
            item.setVisible(true);
            MenuItem loginout = menu.findItem(R.id.nav_loginout);
            loginout.setTitle("تسجيل خروج");
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_task) {
            // Handle the camera action
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent();
            intent.setClass( getBaseContext(), HelpActivity.class );
            startActivity( intent );
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent();
            intent.setClass( getBaseContext(), SettingActivity.class );
            startActivity( intent );

        } else if (id == R.id.nav_loginout)
        {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            // get menu from navigationView
            Menu menu = navigationView.getMenu();
            MenuItem loginout = menu.findItem(R.id.nav_loginout);
            if(loginout.getTitle().equals("تسجيل خروج"))
            {
                //move to login
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), LoginActivty.class);
                startActivity(intent);
                writeSharedPreference("",LOGIN_KEY,LOGIN);

            }
            else
            {
                //move to login
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), LoginActivty.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.share_text));
            shareIntent.setType("text/plain");
            startActivity(shareIntent);

        } else if (id == R.id.nav_send) {

            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            }
            catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String readSharedPreference(String key,String s )
    {
        SharedPreferences sharedPref =getBaseContext().getSharedPreferences(key,MODE_PRIVATE);
        //"" is default_value if no vaule
        String loginType = sharedPref .getString(s,"");

        return loginType;
    }
    public  void  writeSharedPreference(String loginType,String key,String s )
    {
        SharedPreferences sharedPref =getBaseContext().getSharedPreferences(key,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(s, loginType);
        editor.commit();
    }
}
