package com.example.jarekb.snmpviewer.networking;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jarekb on 12/8/16.
 */

public class HttpURLBuilder {
    String hostname, port, path;
    HashMap<String, String> getMethodParams;

    String url;

    public HttpURLBuilder(String hostname, String port, String path)
    {
        this.hostname = hostname;
        this.port = port;
        this.path = path;
        this.getMethodParams = new HashMap<>();
    }

    public void addGETParam(String key, String val)
    {
        getMethodParams.put(key, val);
    }
    private void prepareURL()
    {
        url = "http://" + hostname + ":" + port + "/" + path + "/";
        if(!getMethodParams.isEmpty())
        {
            url += "?";

            for(HashMap.Entry<String, String> entry : getMethodParams.entrySet())
            {
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
    }

    public URL getURL() throws MalformedURLException
    {
        prepareURL();
        return new URL(url);
    }
    public String getURLString()
    {
        prepareURL();
        return url;
    }

}
