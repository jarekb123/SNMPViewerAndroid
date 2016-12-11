package com.example.jarekb.snmpviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jarekb.snmpviewer.preferences.MyPreferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);

        Button buttonOK = (Button) findViewById(R.id.button2);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get();
            }
        });

        EditText editOID = (EditText) findViewById(R.id.editTextOID);
        editOID.setRawInputType(Configuration.KEYBOARD_QWERTY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                Intent intent = new Intent(this, MyPreferences.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    public void get() {

        String oid;
        EditText editTextOID = (EditText) findViewById(R.id.editTextOID);
        oid = editTextOID.getText().toString();

        RadioGroup methodsGroup = (RadioGroup) findViewById(R.id.radio_method);
        int methodID = 0;

        switch (methodsGroup.getCheckedRadioButtonId())
        {
            case R.id.radio_get:
                methodID = 0;
                break;
            case R.id.radio_get_next:
                methodID = 1;
                break;
            case R.id.radio_table_view:
                methodID = 2;
                break;
            default:
                methodID = 0;
                break;
        }

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        Intent intent;
        if (methodID == 2)
        {
            intent = new Intent(this, SNMPTableView.class);

        }
        else {
            intent = new Intent(this, SNMPObjectDisplay.class);
        }
        // przekazuje dodatkowe informacje do intenta
        intent.putExtra("oid", oid);
        intent.putExtra("action", methodID);
        startActivity(intent);

    }
}
