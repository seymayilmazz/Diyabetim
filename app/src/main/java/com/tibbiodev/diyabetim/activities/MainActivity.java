package com.tibbiodev.diyabetim.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.tibbiodev.diyabetim.async.FetchFoodListTask;
import com.tibbiodev.diyabetim.async.FetchUsefulInfoListTask;
import com.tibbiodev.diyabetim.data.DiyabetimContract;
import com.tibbiodev.diyabetim.fragments.BekleyenOlaylarFragment;
import com.tibbiodev.diyabetim.fragments.BesinTablosuFragment;
import com.tibbiodev.diyabetim.fragments.FaydaliBilgilerFragment;
import com.tibbiodev.diyabetim.fragments.GerceklestirilenOlaylarFragment;
import com.tibbiodev.diyabetim.fragments.IlacSaatlerimFragment;
import com.tibbiodev.diyabetim.fragments.InsulinSaatlerimFragment;
import com.tibbiodev.diyabetim.R;
import com.tibbiodev.diyabetim.fragments.SekerOlcumlerimFragment;
import com.tibbiodev.diyabetim.fragments.YaklasanOlaylarFragment;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          //      registerForContextMenu(fab);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new YaklasanOlaylarFragment();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();


        getSupportActionBar().setTitle(getString(R.string.yaklasan_olaylar));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext()
        );
        TextView textViewKisiIsmi = (TextView) findViewById(R.id.kisiIsmi);
        textViewKisiIsmi.setText(sharedPreferences.getString("kisi_ismi", ""));

        Intent intent = new Intent();
        intent.setAction("com.tibbiodev.diyabetim");
        sendBroadcast(intent);

        //startActivity(new Intent(MainActivity.this, AlarmActivity.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
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

        if(id == R.id.nav_ayarlar){
            startActivity(new Intent(getApplicationContext(), AyarlarActivity.class));
            return true;
        }
        else if(id == R.id.nav_cikisYap){
            showLogoutDialog();
            return true;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = null;

        fab.setVisibility(View.VISIBLE);
        if (id == R.id.nav_yaklasan_olaylar) {
            fragment = new YaklasanOlaylarFragment();
            fab.setVisibility(View.INVISIBLE);
        }
        else if(id == R.id.nav_seker_olcumlerim){
            fragment = new SekerOlcumlerimFragment();
        }
        else if (id == R.id.nav_insulin_saatlerim) {
            fragment = new InsulinSaatlerimFragment();
        }
        else if (id == R.id.nav_ilac_saatlerim) {
            fragment = new IlacSaatlerimFragment();
        }
        else if(id == R.id.nav_besin_tablosu){
            FetchFoodListTask fetchFoodListTask = new FetchFoodListTask(getApplicationContext());
            fetchFoodListTask.execute();
            fragment = new BesinTablosuFragment();
            fab.setVisibility(View.INVISIBLE);
        }
        else if(id == R.id.nav_faydali_bilgiler){
            FetchUsefulInfoListTask fetchUsefulInfoListTask =
                    new FetchUsefulInfoListTask(getApplicationContext());
            fetchUsefulInfoListTask.execute();
            fragment = new FaydaliBilgilerFragment();
            fab.setVisibility(View.INVISIBLE);
        }


        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutDialog(){
        String message  = getResources().getString(R.string.dialog_logout_message);
        String positiveText = getResources().getString(R.string.dialog_yes_button);
        String negativeText = getResources().getString(R.string.dialog_no_button);
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)

                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(
                                getApplicationContext()
                        ).edit();
                        editor.putBoolean("auto_login", false);
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), BaslangicActivity.class));
                        MainActivity.this.finish();
                    }
                })

                .setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }
}
