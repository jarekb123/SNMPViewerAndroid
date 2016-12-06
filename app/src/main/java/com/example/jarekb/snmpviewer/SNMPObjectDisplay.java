package com.example.jarekb.snmpviewer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jarekb on 12/6/16.
 */

public class SNMPObjectDisplay extends Activity {
    String jsonString;
    JSONObject snmpObject;

    SNMPObjectDisplay(String jsonString) {
        this.jsonString = jsonString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            snmpObject = new JSONObject(jsonString);
        }
        catch (JSONException jsonEx)
        {
            Toast.makeText(getApplicationContext(), "Wrong response from server", Toast.LENGTH_LONG);
        }

    }
}
