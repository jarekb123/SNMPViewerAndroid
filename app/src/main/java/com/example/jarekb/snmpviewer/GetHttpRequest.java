package com.example.jarekb.snmpviewer;

import android.os.AsyncTask;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jarekb on 12/6/16.
 */

public class GetHttpRequest {
    private class HttpClient extends AsyncTask<String, Boolean, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://192.168.0.19:5000/get_next/?oid=.1.3.6.1.2.1.1.6.0");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                urlConnection.setRequestMethod("GET");

                InputStream is = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String result = "", data="";

                while ((data = reader.readLine()) != null){
                    result += data + "\n";
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}
