package com.example.jarekb.snmpviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonOK = (Button) findViewById(R.id.button2);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get();
            }
        });

        spinner = (Spinner) findViewById(R.id.spinnerAction);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.snmp_methods, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
        
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
                Intent intent = new Intent(this, Preferences.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    public void get() {

        String serverIP;
        EditText editTextIP = (EditText) findViewById(R.id.editTextIP);
        serverIP = editTextIP.getText().toString();

        String oid;
        EditText editTextOID = (EditText) findViewById(R.id.editTextOID);
        oid = editTextOID.getText().toString();

        Intent intent = new Intent(this, SNMPObjectDisplay.class);
        // przekazuje dodatkowe informacje do intenta
        intent.putExtra("action", spinner.getSelectedItemId());
        intent.putExtra("serverIP", serverIP);
        intent.putExtra("oid", oid);

        startActivity(intent);
        System.out.println("id: " + spinner.getSelectedItemId());
    }
}
