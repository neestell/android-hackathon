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
    UserModel userModel = null;
    SharedPreferences preferences;
    String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_settings);
        username = getIntent().getStringExtra(Constants.USER_NAME);

        preferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        String usersString = preferences.getString(Constants.USERS, null);
        ArrayList<String> usersJson;
        Gson gson = new Gson();

        if (TextUtils.isEmpty(usersString)){

            usersJson = new ArrayList<String>();
            Log.d("qwqw", "no users found");

        }else {

            Type listType = new TypeToken<List<String>>() {}.getType();
            usersJson = gson.fromJson(usersString, listType);
            Log.d("qwqw", "users exists: " + usersJson.size());

        }

        for (String user: usersJson){
            UserModel userModel = gson.fromJson(user, UserModel.class);
            if (userModel.getName().equals(username)){
                this.userModel = userModel;
                Log.d("qwqw", "old user");

                break;
            }
        }
        if (userModel == null){

            userModel = new UserModel(username);
            usersJson.add(gson.toJson(userModel));
            preferences.edit()
                    .putString(Constants.USERS, gson.toJson(usersJson))
                    .apply();
            Log.d("qwqw", "new user, users updated");

        }

        settingsRecycler = findViewById(R.id.recycler_settings);
        settingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingsRecycler.setHasFixedSize(true);
        settingsRecycler.setAdapter(settingsAdapter = new SettingsAdapter());

    }
}
