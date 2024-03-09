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
        Button buttonOK = view.findViewById(R.id.buttontimeOK);

        // Set click listener for the OK button
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Create AlertDialog with TimePickerDialog as its content
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onTimeSet(TimePicker view, int selectedHourOfDay, int selectedMinute) {
        if (getParentFragment() instanceof CadastrarDiaFragment) {
            // Obtenha a data selecionada no DatePicker
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("selected_date", Context.MODE_PRIVATE);
            int year = sharedPreferences.getInt("year", 0);
            int month = sharedPreferences.getInt("month", 0);
            int dayOfMonth = sharedPreferences.getInt("day", 0);

            //((CadastrarDiaFragment) getParentFragment()).persistirTarefa(year, month, dayOfMonth, selectedHourOfDay, selectedMinute);
        }
    }
}
