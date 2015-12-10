package edu.purdue.androidforcefive.evtcollab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.purdue.androidforcefive.evtcollab.Controller.LogonController;
import edu.purdue.androidforcefive.evtcollab.R;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private TextView txtUsername;
    private TextView txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mPreferences = getSharedPreferences("SharedCalendar", Context.MODE_PRIVATE);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPassword = (TextView) findViewById(R.id.txtPassword);


        txtUsername.setText(mPreferences.getString("username", ""));
        txtPassword.setText(mPreferences.getString("password", ""));


        setSupportActionBar(toolbar);

        ((Button) findViewById(R.id.btnSaveSettings)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LogonController.getInstance().checkCredentialValidity(txtUsername.getText().toString(), txtPassword.getText().toString())) {
                    SharedPreferences.Editor edit = mPreferences.edit();
                    edit.clear();
                    edit.putString("username", txtUsername.getText().toString());
                    edit.putString("password", txtPassword.getText().toString());
                    edit.commit();
                    Toast.makeText(SettingsActivity.this, "Account saved.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
