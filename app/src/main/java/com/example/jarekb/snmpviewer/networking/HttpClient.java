package com.example.jarekb.snmpviewer.networking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * Created by jarekb on 12/6/16.
 */

public class HttpClient {

    private String result;
    private int responseCode;
    private URL mUrl;

   public void sendHttpRequest(String url) throws IOException
    {
        result = "";
        mUrl = new URL(url);
        HttpURLConnection urlConnection = null;



        urlConnection = (HttpURLConnection) mUrl.openConnection();
        urlConnection.connect();
        urlConnection.setRequestMethod("GET");
        responseCode = urlConnection.getResponseCode();
        System.out.println("responseCode: " + responseCode);
        if(responseCode != 200)
            return;

        InputStream is = urlConnection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String data = "";

        while ((data = reader.readLine()) != null) {
            result += data;
        }
    }

    public String getResult() {
        return result;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public URL getmUrl() {
        return mUrl;
    }
}

