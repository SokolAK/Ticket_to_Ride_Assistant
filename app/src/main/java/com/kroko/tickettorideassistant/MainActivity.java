package com.kroko.TicketToRideAssistant;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;
import android.content.Intent;
import androidx.core.view.GravityCompat;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                        drawer,
                        toolbar,
                        R.string.nav_open_drawer,
                        R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Game game = ((TtRA_Application) getApplication()).game;
        game.prepare((int) 0);
        Player player = ((TtRA_Application) getApplication()).player;
        player.prepare(game);


        DbHelper dbHelper = new DbHelper(this,"TtRADatabase.db",2);
        try {
            dbHelper.checkDatabase();
        }
        catch (Exception e) {}
        try {
            dbHelper.openDatabase();
        }
        catch (Exception e) {}

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String filePath = db.getPath();
        db.close();
        db.rawQuery("SELECT * FROM Routes", null);


        Fragment fragment = new TopFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, fragment);
        ft.commit();

    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        return onNavigationItemSelected(id);
    }

    public boolean onNavigationItemSelected(int id) {
        Fragment fragment = null;
        Intent intent = null;
        switch(id){
            case R.id.nav_draw_cards:
                fragment = new DrawFragment();
                break;
            case R.id.nav_build_route:
                fragment = new BuildRoutesFragment();
                break;
            case R.id.nav_show_routes:
                fragment = new ShowRoutesFragment();
                break;
            case R.id.nav_build_station:
                fragment = new StationFragment();
                break;
            case R.id.nav_help:
                intent = new Intent(this, HelpActivity.class);
                break;
            default:
                fragment = new TopFragment();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        } else {
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        onNavigationItemSelected(0);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
