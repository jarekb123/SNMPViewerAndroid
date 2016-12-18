package com.example.jarekb.snmpviewer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarekb.snmpviewer.networking.HttpClient;
import com.example.jarekb.snmpviewer.networking.HttpURLBuilder;
import com.example.jarekb.snmpviewer.preferences.MyPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by jarekb on 12/6/16.
 */

public class SNMPObjectDisplay extends AppCompatActivity {

    String proxyIP, proxyPort;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

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



                HttpClient snmpQueryRequest = new HttpClient();
                HttpURLBuilder queryURLBuilder = new HttpURLBuilder(proxyIP, proxyPort, params[0]);
                queryURLBuilder.addGETParam("oid", params[1]);
                snmpQueryRequest.sendHttpRequest(queryURLBuilder.getURLString());

                if(snmpQueryRequest.getResponseCode() != 200)
                    return null;

                return snmpQueryRequest.getResult();

            } catch (IOException ioe) {
                publishProgress(-1);
                ioe.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]==-1) {
                Toast.makeText(SNMPObjectDisplay.this, "Error during connecting to proxy server", Toast.LENGTH_LONG).show();
                finish();
            }
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.object_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        proxyIP = sharedPreferences.getString(MyPreferences.PREF_KEY_PROXY_IP, "");
        proxyPort = sharedPreferences.getString(MyPreferences.PREF_KEY_PROXY_PORT, "");


        String method = "get";
        if(action == 0)
            method = "get";
        else if(action == 1)
            method = "get_next";

        try {

            //parsowanie JSONA do SNMPObject
            String jsonString = new GetObject().execute(method, mOID).get();
            if (jsonString == null) {
                throw new Exception("Null JSON response");
            }
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray array = new JSONArray();
            SNMPObject snmpObject = new SNMPObject(jsonObject);

            // wpisywanie do GUI
            TextView textViewName = (TextView) findViewById(R.id.objNameVal);
            textViewName.setText(snmpObject.getName());

            TextView textViewOid = (TextView) findViewById(R.id.objOIDVal);
            textViewOid.setText(snmpObject.getOid());

            TextView textViewType = (TextView) findViewById(R.id.objTypeVal);
            textViewType.setText(snmpObject.getType());

            TextView textViewValue = (TextView) findViewById(R.id.objValueVal);
            textViewValue.setText(snmpObject.getValue());

        }
        catch (Exception jsonEx)
        {
            jsonEx.printStackTrace();
            Toast.makeText(getApplicationContext(), "Wrong response from server", Toast.LENGTH_LONG).show();
            finish();
        }

    }
}
