package com.kroko.TicketToRideAssistant.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.navigation.NavigationView;
import com.kroko.TicketToRideAssistant.Logic.Game;
import com.kroko.TicketToRideAssistant.Logic.Player;
import com.kroko.TicketToRideAssistant.R;
import com.kroko.TicketToRideAssistant.Logic.TtRA_Application;

import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;

import androidx.core.view.GravityCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //private Game game = ((TtRA_Application) getApplication()).game;
    //private Player player = ((TtRA_Application) getApplication()).player;

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
        Player player = ((TtRA_Application) getApplication()).player;
        game.prepare(0);
        player.setGame(game);
        player.prepare();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, new TopFragment());
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
        switch (id) {
            case R.id.nav_draw_cards:
                fragment = new DrawFragment();
                break;
            case R.id.nav_show_routes:
                fragment = new ShowBuiltRoutesFragment();
                break;
            case R.id.nav_show_stations:
                fragment = new ShowBuiltStationsFragment();
                break;
            case R.id.nav_show_tickets:
                fragment = new ShowTicketsFragment();
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
            ft.addToBackStack(null);
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
        getSupportFragmentManager().popBackStack();
        /*
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        onNavigationItemSelected(0);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        */
    }

    public void onClickBuildRoute(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new BuildRouteFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
    public void onClickBuildStation(View view) {
        Player player = ((TtRA_Application) getApplication()).player;
        if(player.getNumberOfStations() > 0) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new BuildStationFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
    }
    public void onClickDrawTicket(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new DrawTicketFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}
