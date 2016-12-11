package com.example.jarekb.snmpviewer.preferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jarekb.snmpviewer.R;

/**
 * Created by jarekb on 12/8/16.
 */

public class MyPreferences extends AppCompatActivity {

    // static keys of preferences fields
    static public final String PREF_KEY_SNMP_AGENT_IP = "pref_key_snmp_agent_ip";
    static public final String PREF_KEY_SNMP_AGENT_PORT = "pref_key_snmp_agent_port";
    static public final String PREF_KEY_PROXY_IP = "pref_key_proxy_ip";
    static public final String PREF_KEY_PROXY_PORT = "pref_key_proxy_port";


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
            finish();
            return true;
        }
        return false;
    }
}
