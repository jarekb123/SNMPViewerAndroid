package com.example.jarekb.snmpviewer.preferences;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * Created by jarekb on 12/8/16.
 */

public final class MyEditTextPreference extends EditTextPreference {
    public MyEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditTextPreference(Context context) {
        super(context);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setSummary(getText());
    }
}
