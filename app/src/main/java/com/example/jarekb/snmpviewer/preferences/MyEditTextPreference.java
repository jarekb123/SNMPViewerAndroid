package com.example.jarekb.snmpviewer.preferences;

import android.content.Context;
import android.content.res.Configuration;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * Created by jarekb on 12/8/16.
 */

public final class MyEditTextPreference extends EditTextPreference {
    public MyEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        getEditText().setRawInputType(Configuration.KEYBOARD_QWERTY);
    }

    public MyEditTextPreference(Context context) {
        super(context);

        getEditText().setRawInputType(Configuration.KEYBOARD_QWERTY);
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setSummary(getText());
    }
}
