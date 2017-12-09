package com.rosberry.hackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class SettingsActivity extends AppCompatActivity {
    RecyclerView settingsRecycler;
    SettingsAdapter settingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_settings);
        settingsRecycler = findViewById(R.id.recycler_settings);
        settingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingsRecycler.setHasFixedSize(true);
        settingsRecycler.setAdapter(settingsAdapter = new SettingsAdapter());
    }
}
