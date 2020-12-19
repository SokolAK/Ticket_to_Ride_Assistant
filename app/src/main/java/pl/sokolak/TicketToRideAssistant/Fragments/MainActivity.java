package pl.sokolak.TicketToRideAssistant.Fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.navigation.NavigationView;

import pl.sokolak.TicketToRideAssistant.Domain.Game;
import pl.sokolak.TicketToRideAssistant.Domain.Player;
import pl.sokolak.TicketToRideAssistant.R;
import pl.sokolak.TicketToRideAssistant.TtRA_Application;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.view.GravityCompat;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Game game;
    private Player player;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer_nav, menu);
        MenuItem item = menu.findItem(R.id.nav_draw_cards_warehouse);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        invalidateOptionsMenu();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int gamePosition = intent.getIntExtra("gamePosition", 0);
        //String gameTitle = getResources().getStringArray(R.array.games)[gameId];

        player = ((TtRA_Application) getApplication()).player;
        game = Game.create(this, gamePosition);
        ((TtRA_Application) getApplication()).game = game;

        game.setPlayer(player);
        player.setGame(game);
        player.prepare();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_top);
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

        View header = LayoutInflater.from(this).inflate(R.layout.header_nav, null);
        navigationView.removeHeaderView(navigationView.getHeaderView(0));
        navigationView.addHeaderView(header);
        TextView text = header.findViewById(R.id.nav_header_game_text);
        text.setText(game.getTitle());

        hideMenuItems();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, new TopFragment());
        ft.commit();
    }

    private void hideMenuItems() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        if(!game.isStationsAvailable()) {
            nav_Menu.findItem(R.id.nav_show_stations).setVisible(false);
        }
        if(!game.isWarehousesAvailable()) {
            nav_Menu.findItem(R.id.nav_draw_cards_warehouse).setVisible(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        return onNavigationItemSelected(id);
    }

    private boolean onNavigationItemSelected(int id) {
        Fragment fragment = null;
        Intent intent = null;
        switch (id) {
            case R.id.nav_points:
                fragment = new PointsFragment();
                break;
            case R.id.nav_draw_cards:
                fragment = new DrawFragment(game.getMaxNoOfCardsToDraw(), getString(R.string.nav_drawCards));
                break;
            case R.id.nav_draw_cards_warehouse:
                fragment = new DrawFragment(0, getString(R.string.nav_pickCardsFromWarehouse));
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
            case R.id.nav_tickets_decks:
                fragment = new ChooseDecksOfTicketsFragment();
                break;
            case R.id.nav_help:
                intent = new Intent(this, HelpActivity.class);
                break;
            case R.id.nav_exit:
                exitMessageBox();
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

    //To prevent java.lang.NullPointerException:
    //Attempt to invoke virtual method 'boolean android.content.Intent.migrateExtraStreamToClipData()' on a null object reference
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent != null)
            super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    private void exitMessageBox() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(R.string.exit_message);
        dlgAlert.setTitle(R.string.app_name);
        dlgAlert.setPositiveButton(R.string.no, null);
        dlgAlert.setNegativeButton(R.string.yes, (dialog, which) -> {
            finishAffinity();
            System.exit(0);
        });
        ;
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
