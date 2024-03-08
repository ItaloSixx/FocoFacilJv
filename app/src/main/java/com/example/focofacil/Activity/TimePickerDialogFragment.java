package com.example.focofacil.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.focofacil.R;
import java.util.Calendar;

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(requireContext()));

        // Inflate the layout containing the button
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_time_picker, null, false);

        // Initialize the OK button
        Button buttonOK = view.findViewById(R.id.buttonOK);

        // Set click listener for the OK button
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic to be executed when the OK button is clicked
                // Here you can capture the selected time and do something with it
                dismiss();
            }
        });

        // Create AlertDialog with TimePickerDialog as its content
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Here you can do something with the selected time
        SharedPreferences.Editor editor = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
        editor.putInt("hourOfDay", hourOfDay);
        editor.putInt("minute", minute);
        editor.apply();
    }
}
