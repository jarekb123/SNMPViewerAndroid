package com.example.jarekb.snmpviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
