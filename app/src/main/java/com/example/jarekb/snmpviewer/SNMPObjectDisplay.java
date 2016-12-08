package com.example.jarekb.snmpviewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by jarekb on 12/6/16.
 */

public class SNMPObjectDisplay extends AppCompatActivity {
    String jsonString;
    JSONObject snmpObject;

    String name, oid, type, value;

private class GetObject extends AsyncTask <String, Integer, String>
{
    private ProgressDialog progressDialog;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(SNMPObjectDisplay.this);
        progressDialog.setMessage("Downloading SNMP Object...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return HttpClient.getHttpRequest(params[0]);
        } catch (IOException ioe) {
            publishProgress(-1);
            ioe.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(values[0]==-1)
            Toast.makeText(SNMPObjectDisplay.this, "Error during connecting to proxy server", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
    }
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snmp_object);
        Intent intent = getIntent();
        long action = intent.getLongExtra("action", 0);
        String mOID = intent.getStringExtra("oid");
        String serverIP = intent.getStringExtra("serverIP");
        String method = "get";
        if(action == 0)
            method = "get";
        else if(action == 1)
            method = "get_next";

        try {
            jsonString = new GetObject().execute("http://" + serverIP +":5000/" + method + "/?oid="+mOID).get();
            snmpObject = new JSONObject(jsonString);
            name = snmpObject.getString("name");
            oid = snmpObject.getString("oid");
            type = snmpObject.getString("type");
            value = snmpObject.getString("val");

            // wpisywanie do GUI
            TextView textViewName = (TextView) findViewById(R.id.objNameVal);
            textViewName.setText(name);
            TextView textViewOid = (TextView) findViewById(R.id.objOIDVal);
            textViewOid.setText(oid);
            TextView textViewType = (TextView) findViewById(R.id.objTypeVal);
            textViewType.setText(type);
            TextView textViewValue = (TextView) findViewById(R.id.objValueVal);
            textViewValue.setText(value);

        }
        catch (Exception jsonEx)
        {
            jsonEx.printStackTrace();
            Toast.makeText(getApplicationContext(), "Wrong response from server", Toast.LENGTH_LONG).show();
            moveTaskToBack(true);
        }

    }
}
