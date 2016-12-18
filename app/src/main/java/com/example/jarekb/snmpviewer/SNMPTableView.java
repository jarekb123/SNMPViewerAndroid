package com.example.jarekb.snmpviewer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.jarekb.snmpviewer.networking.HttpClient;
import com.example.jarekb.snmpviewer.networking.HttpURLBuilder;
import com.example.jarekb.snmpviewer.preferences.MyPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jarekb on 12/10/16.
 */

public class SNMPTableView extends AppCompatActivity {
    JSONArray tableArray;
    ArrayList<ArrayList<SNMPObject>> rows;
    String oid;
    int rowIndex;
    TableLayout tableLayout;
    Button nextBtn, previousBtn;
    boolean dataLoaded;

    public SNMPTableView()
    {
        rows = new ArrayList<>();
        rowIndex = 0;
        dataLoaded = false;
    }

    private class LoadTableTask extends AsyncTask<String, Void, JSONArray>
    {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SNMPTableView.this);
            progressDialog.setMessage("Downloading Table...");
            progressDialog.show();

        }

        @Override
        protected JSONArray doInBackground(String... objects) {
            return loadJSONArray();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            try {
                tableArray = jsonArray;
                parseJSONArray(tableArray);

                // dodawanie pierwszego wiersza do gui

                displayRow(0);
                dataLoaded = true;
                progressDialog.dismiss();



            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Intent intent = getIntent();
        oid = intent.getStringExtra("oid");

        Toolbar toolbar = (Toolbar) findViewById(R.id.table_activity_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tableLayout = (TableLayout) findViewById(R.id.table_view);

        nextBtn = (Button) findViewById(R.id.btnNextRow);
        previousBtn = (Button) findViewById(R.id.btnPreviousRow);

        nextBtn.setVisibility(View.INVISIBLE);
        previousBtn.setVisibility(View.INVISIBLE);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRow(++rowIndex);
            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRow(--rowIndex);
            }
        });

        try {
            if(dataLoaded == false) {
                new LoadTableTask().execute();
                System.out.println("zaladowano jsona");
            }



        }
        catch (Exception ee) {
            ee.printStackTrace();
        }


    }

    private JSONArray loadJSONArray()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String proxyIP = preferences.getString(MyPreferences.PREF_KEY_PROXY_IP, "");
        String proxyPort = preferences.getString(MyPreferences.PREF_KEY_PROXY_PORT, "");
        String agentIP = preferences.getString(MyPreferences.PREF_KEY_SNMP_AGENT_IP, "");
        String agentPort = preferences.getString(MyPreferences.PREF_KEY_SNMP_AGENT_PORT, "");
        String communityName = preferences.getString(MyPreferences.PREF_KEY_SNMP_COMMUNITY_NAME, "");


        try {

            HttpClient httpClient = new HttpClient();

            HttpURLBuilder queryUrlBuilder = new HttpURLBuilder(proxyIP, proxyPort, "get_table");
            queryUrlBuilder.addGETParam("oid", oid);
            httpClient.sendHttpRequest(queryUrlBuilder.getURLString());

            String jsonString = httpClient.getResult();

            JSONArray jsonArray = new JSONArray(jsonString);
            return jsonArray;

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseJSONArray(JSONArray jsonArray) throws JSONException {
        int len = jsonArray.length();
        for(int i = 0; i<len; i++) {
            JSONArray rowArray = jsonArray.getJSONArray(i);

            ArrayList<SNMPObject> tempRow = new ArrayList<>();

            int numOfObjectsInRow = rowArray.length();
            for(int j = 0; j<numOfObjectsInRow; j++) {
                SNMPObject snmpObject = new SNMPObject(rowArray.getJSONObject(j));
                tempRow.add(snmpObject);
            }
            rows.add(tempRow);
        }
    }




    private void displayRow(int index)
    {
        tableLayout.removeAllViews();
        if(index == rows.size() - 1)
            nextBtn.setVisibility(View.INVISIBLE);
        else nextBtn.setVisibility(View.VISIBLE);
        if(index == 0)
            previousBtn.setVisibility(View.INVISIBLE);
        else previousBtn.setVisibility(View.VISIBLE);
        TableRow headRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.table_row, null);
        TextView nameHead = (TextView) headRow.findViewById(R.id.field_left);
        nameHead.setText("NAME");
        TextView valueHead = (TextView) headRow.findViewById(R.id.field_right);
        valueHead.setText("VALUE");
        Collections.sort(rows.get(index));
        tableLayout.addView(headRow);
        for(int i = 0; i<rows.get(index).size(); i++) {
            TableRow dataRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.table_row, null);
            TextView nameField = (TextView) dataRow.findViewById(R.id.field_left);
            nameField.setText(rows.get(index).get(i).getName());
            TextView valueField = (TextView) dataRow.findViewById(R.id.field_right);
            valueField.setText(rows.get(index).get(i).getValue());
            tableLayout.addView(dataRow);
        }
    }
}
