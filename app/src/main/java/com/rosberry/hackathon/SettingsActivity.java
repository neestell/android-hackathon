package com.rosberry.hackathon;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    RecyclerView settingsRecycler;
    SettingsAdapter settingsAdapter;
    Storage storage;
    UserModel userModel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_settings);
        storage = new Storage(getApplicationContext());
        userModel = storage.prepareUserModel(getIntent().getStringExtra(Constants.USER_NAME), true);

        settingsRecycler = findViewById(R.id.recycler_settings);
        settingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingsRecycler.setHasFixedSize(true);
        settingsRecycler.setAdapter(settingsAdapter = new SettingsAdapter());

    }


}
