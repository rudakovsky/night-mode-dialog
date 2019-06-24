package com.komparato.informer.wear.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import com.komparato.informer.wear.R;

public class NightModeDialog extends DialogFragment {

    public final static String NIGHT_HOUR_SET_PREF = "nightHoursSet";
    public final static String SLEEP_HOUR_PREF = "sleepHour";
    public final static String WAKEUP_HOUR_PREF = "wakeupHour";
    public final static int SLEEP_HOUR_DEFAULT = 23;
    public final static int WAKEUP_HOUR_DEFAULT = 7;
    
    public interface NightModeChangeListener {
        void onNightModeChange();
    }

    NightModeChangeListener mListener;
    SharedPreferences mPreferences;
    private NumberPicker mSleepPicker, mWakeupPicker;

    @SuppressLint("ValidFragment")
    public NightModeDialog(NightModeChangeListener listener) {
        mListener = listener;
    }

    public NightModeDialog() {
        // Do not use default constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.night_mode_layout, null);
        builder.setView(view);
        mSleepPicker = view.findViewById(R.id.sleepPickerId);
        mSleepPicker.setMinValue(0);
        mSleepPicker.setMaxValue(23);
        mWakeupPicker = view.findViewById(R.id.wakeupPickerId);
        mWakeupPicker.setMinValue(0);
        mWakeupPicker.setMaxValue(23);

        if (mPreferences.getBoolean(NIGHT_HOUR_SET_PREF, false)) {
            mSleepPicker.setValue(mPreferences.getInt(SLEEP_HOUR_PREF, SLEEP_HOUR_DEFAULT));
            mWakeupPicker.setValue(mPreferences.getInt(WAKEUP_HOUR_PREF, WAKEUP_HOUR_DEFAULT));
        } else {
            mSleepPicker.setValue(SLEEP_HOUR_DEFAULT);
            mWakeupPicker.setValue(WAKEUP_HOUR_DEFAULT);
        }

        builder.setTitle(R.string.night_mode_title);
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("preference_night_mode", false);
                editor.apply();
                mListener.onNightModeChange();
                dismiss();
            }
        });
        builder.setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(NIGHT_HOUR_SET_PREF, true);
                editor.putInt(SLEEP_HOUR_PREF, mSleepPicker.getValue());
                editor.putInt(WAKEUP_HOUR_PREF, mWakeupPicker.getValue());
                editor.apply();
                mListener.onNightModeChange();
                dismiss();
            }
        });
        
        return builder.create();
    }

}
