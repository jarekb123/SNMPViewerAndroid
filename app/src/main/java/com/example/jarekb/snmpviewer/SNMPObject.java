package com.example.jarekb.snmpviewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by jarekb on 12/10/16.
 */

public class SNMPObject implements Comparable<SNMPObject> {
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

    @Override
    public int compareTo(SNMPObject snmpObject) {
        int compareObjects = oid.compareTo(snmpObject.oid);
        return compareObjects;
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

    public static SNMPObjectsComparator getComparator() {
        return new SNMPObjectsComparator();
    }

    private static class SNMPObjectsComparator implements Comparator<SNMPObject> {
        @Override
        public int compare(SNMPObject snmpObject, SNMPObject t1) {
            return snmpObject.compareTo(t1);
        }
    }
}
