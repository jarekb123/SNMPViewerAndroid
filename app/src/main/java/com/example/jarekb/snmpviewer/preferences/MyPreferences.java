package com.example.jarekb.snmpviewer.preferences;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jarekb.snmpviewer.R;
import com.example.jarekb.snmpviewer.SNMPObjectDisplay;
import com.example.jarekb.snmpviewer.networking.HttpClient;
import com.example.jarekb.snmpviewer.networking.HttpURLBuilder;

import java.io.IOException;

/**
 * Created by jarekb on 12/8/16.
 */

public class MyPreferences extends AppCompatActivity {

    // static keys of preferences fields
    static public final String PREF_KEY_SNMP_AGENT_IP = "pref_key_snmp_agent_ip";
    static public final String PREF_KEY_SNMP_AGENT_PORT = "pref_key_snmp_agent_port";
    static public final String PREF_KEY_PROXY_IP = "pref_key_proxy_ip";
    static public final String PREF_KEY_PROXY_PORT = "pref_key_proxy_port";
    static public final String PREF_KEY_SNMP_COMMUNITY_NAME = "pref_key_snmp_community_name";

    String snmpIP, snmpPort, proxyIP, proxyPort, community_name;


    private class ConnectToServerTask extends AsyncTask<Void, Integer, Boolean>
    {
        private ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MyPreferences.this);
            progressDialog.setMessage("Checking connection with proxy server...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {



                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyPreferences.this);
                snmpIP = sharedPreferences.getString(MyPreferences.PREF_KEY_SNMP_AGENT_IP, "");
                snmpPort = sharedPreferences.getString(MyPreferences.PREF_KEY_SNMP_AGENT_PORT, "");
                proxyIP = sharedPreferences.getString(MyPreferences.PREF_KEY_PROXY_IP, "");
                proxyPort = sharedPreferences.getString(MyPreferences.PREF_KEY_PROXY_PORT, "");
                community_name = sharedPreferences.getString(MyPreferences.PREF_KEY_SNMP_COMMUNITY_NAME, "");

                HttpURLBuilder httpURLBuilder = new HttpURLBuilder(proxyIP, proxyPort, "init");
                httpURLBuilder.addGETParam("community_name", community_name);
                httpURLBuilder.addGETParam("address", snmpIP);
                httpURLBuilder.addGETParam("port", snmpPort);

                HttpClient httpClient = new HttpClient();
                httpClient.sendHttpRequest(httpURLBuilder.getURLString());

                if (httpClient.getResponseCode() != 200)
                    throw new Exception("Connecting to proxy server FAILED!!!");

                String result = httpClient.getResult();
                System.out.println(result);
                if (result.equals("OK!")) {
                    return true;
                } else {
                    throw new Exception("Connecting to proxy server FAILED!!!");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s == true)
                finish();
            else
                Toast.makeText(MyPreferences.this, "Error during connecting to proxy server", Toast.LENGTH_LONG).show();


        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_preferences);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            try {
                 new ConnectToServerTask().execute();
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }
        return false;
    }
}
