package com.example.jarekb.snmpviewer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jarekb on 12/10/16.
 */

public class SNMPObject {
    String name;
    String oid;
    String value;
    String type;

    public SNMPObject(JSONObject jsonObject) throws JSONException
    {
        name = jsonObject.getString("name");
        oid = jsonObject.getString("oid");
        type = jsonObject.getString("type");
        value = jsonObject.getString("val");
    }

    public String getName() {
        return name;
    }

    public String getOid() {
        return oid;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
