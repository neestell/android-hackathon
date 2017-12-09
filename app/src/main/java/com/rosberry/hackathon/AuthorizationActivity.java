package com.rosberry.hackathon;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AuthorizationActivity extends AppCompatActivity {

    Storage storage;
    UserModel userModel;
    Button authorizeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_authorization);
        userModel = (UserModel) getIntent().getSerializableExtra(Constants.USER);
        storage = new Storage(this);
        authorizeButton = findViewById(R.id.button_authorize);
        authorizeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                authorize();
            }
        });

    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    private void authorize() {
        boolean success = true;

        for (IAuthorization item : userModel.authorizationModels) {
            success = success && item.check();
        }

        setResult(success ? Activity.RESULT_OK : Activity.RESULT_CANCELED, new Intent() {

            {
                putExtra(Constants.USER, userModel);
            }
        });
        finish();
    }
}
