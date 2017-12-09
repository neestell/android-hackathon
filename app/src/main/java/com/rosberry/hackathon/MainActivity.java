package com.rosberry.hackathon;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        imageView = findViewById(R.id.image_status);
        storage = new Storage(getApplicationContext());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.op_settings:
                showAuthDialog(new Callback<String>() {

                    @Override
                    public void onResult(final String result) {
                        if (result != null)
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class) {

                                {
                                    putExtra(Constants.USER_NAME, result.trim());
                                    setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                }
                            });
                    }
                });

                return true;
            case R.id.op_auth:
                showAuthDialog(new Callback<String>() {

                    @Override
                    public void onResult(final String result) {
                        if (result != null) {
                            final UserModel userModel = storage.prepareUserModel(result, false);
                            if (userModel != null)
                            startActivityForResult(new Intent(MainActivity.this, AuthorizationActivity.class) {

                                {
                                    putExtra(Constants.USER, userModel);
                                    setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                }
                            }, 100);
                            else {
                                Toast.makeText(getApplicationContext(), "User does not exist", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void showAuthDialog(final Callback<String> callback) {
        new MaterialDialog.Builder(this)
                .title("Authorization")
                .input("User name", null, false, new MaterialDialog.InputCallback() {

                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
                        if (!dialog.isCancelled()) {
                            callback.onResult(input.toString());
                        } else {
                            callback.onResult(null);
                        }
                    }
                })
                .positiveText("Ok")
                .negativeText("Cancel")
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            imageView.setImageResource(R.mipmap.welcome);
            //// TODO: 09.12.2017 user here
        } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
            imageView.setImageResource(R.mipmap.spy);
        }
    }
}
