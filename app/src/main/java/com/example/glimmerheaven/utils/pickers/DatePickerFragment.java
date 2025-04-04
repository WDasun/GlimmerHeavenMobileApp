package com.example.glimmerheaven.utils.pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        Calendar selectedDateCalendar = Calendar.getInstance();
        selectedDateCalendar.set(year,month,day,0,0,0);
        selectedDateCalendar.set(Calendar.MILLISECOND,0);

        long selectedDateInMillis = selectedDateCalendar.getTimeInMillis();

        String selectedDate = day + "/" + (month + 1) + "/" + year;
        Bundle result = new Bundle();
        result.putString("selectedDate", selectedDate);
        result.putLong("selectedDateInMillis", selectedDateInMillis);
        getParentFragmentManager().setFragmentResult("datePickerResult", result);
    }

}
