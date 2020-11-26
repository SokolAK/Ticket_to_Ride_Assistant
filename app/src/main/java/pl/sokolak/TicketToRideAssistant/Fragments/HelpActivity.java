package pl.sokolak.TicketToRideAssistant.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import lombok.SneakyThrows;
import pl.sokolak.TicketToRideAssistant.R;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.nav_help);
        setSupportActionBar(toolbar);

        TextView textVersion = findViewById(R.id.version);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            textVersion.setText("v" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
        }
    }
}
