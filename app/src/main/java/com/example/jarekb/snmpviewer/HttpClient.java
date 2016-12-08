package com.example.jarekb.snmpviewer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jarekb on 12/6/16.
 */

public class HttpClient {
    /**
     * Method sends request and gets request using HTTP GET.
     * @param url URL of the destination of the request
     * @return HTTP GET response
     */
    public static String getHttpRequest(String url) throws IOException {
        URL mUrl;
        HttpURLConnection urlConnection = null;

        mUrl = new URL(url);

        urlConnection = (HttpURLConnection) mUrl.openConnection();
        urlConnection.connect();
        urlConnection.setRequestMethod("GET");

        InputStream is = urlConnection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String result = "";
        String data = "";

        while ((data = reader.readLine()) != null) {
            result += data + "\n";
        }
        return result;

    }
}

