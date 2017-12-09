package com.rosberry.hackathon;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry on 09.12.2017.
 */

public class Storage {

    private final Context ctx;
    private final SharedPreferences preferences;

    public Storage(Context applicationContext) {
        this.ctx = applicationContext;
        this.preferences = ctx.getSharedPreferences(
                ctx.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public UserModel prepareUserModel(String username, boolean addUserIfneeded) {
        UserModel userModel = null;


        String usersString = preferences.getString(Constants.USERS, null);
        ArrayList<String> usersJson;
        Gson gson = new Gson();

        if (TextUtils.isEmpty(usersString)) {

            usersJson = new ArrayList<String>();
            Log.d("qwqw", "no users found");

        } else {

            Type listType = new TypeToken<List<String>>() {

            }.getType();
            usersJson = gson.fromJson(usersString, listType);
            Log.d("qwqw", "users exists: " + usersJson.size());

        }

        for (String user : usersJson) {
            UserModel userModelCached = gson.fromJson(user, UserModel.class);
            if (userModelCached.getName().equals(username)) {
                userModel = userModelCached;
                Log.d("qwqw", "old user");

                break;
            }
        }

        if (userModel == null && addUserIfneeded) {

            userModel = new UserModel(username);
            usersJson.add(gson.toJson(userModel));
            preferences.edit()
                    .putString(Constants.USERS, gson.toJson(usersJson))
                    .apply();
            Log.d("qwqw", "new user, users updated");
        }

        return userModel;
    }

    String getString(String key, String value) {
        return preferences.getString(key, value);
    }

    void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }
}
