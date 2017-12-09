package com.rosberry.hackathon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
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
                new MaterialDialog.Builder(this)
                        .title("Authorization")
                        .input("User name", null, false, new MaterialDialog.InputCallback() {

                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, final CharSequence input) {
                                if (!dialog.isCancelled()){
                                    startActivity(new Intent(MainActivity.this, SettingsActivity.class) {
                                        {
                                            putExtra(Constants.USER_NAME, input.toString().trim());
                                            setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        }
                                    });
                                }
                            }
                        })
                        .positiveText("Ok")
                        .negativeText("Cancel")
                        .show();



                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
