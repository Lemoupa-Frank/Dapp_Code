package com.example.dvote.fabric_gateway.custom_dialog;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static Date startdate;
    public static Date enddate;
    public static Date startdatetemp;
    public static Date enddatetemp;
    /* DatePicker datePicker; DateTime dateTime;
     int year;
     int month;
     int day;*/
    private DateSelectedListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String formattedDateTime = String.format(Locale.getDefault(), "%04d-%02d-%02dT00:00:00Z", year, month, day);
        if (listener != null) {
            listener.onDateSelected(year, month, day);
        }

    }

    public void setListener(DateSelectedListener listener) {
        this.listener = listener;
    }

    public interface DateSelectedListener {
        void onDateSelected(int year, int month, int dayOfMonth);
    }

}

