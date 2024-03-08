package com.example.focofacil.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.focofacil.R;
import java.util.Calendar;
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, year, month, day);
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.fragment_date_picker_dialog, null);
        Button buttonOK = dialogView.findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOKButtonClick(datePickerDialog);
            }
        });
        datePickerDialog.setView(dialogView);
        return datePickerDialog;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ActivityCadastrarDiaViewModel viewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance()).get(ActivityCadastrarDiaViewModel.class);
        viewModel.setDataSelecionada(year, month, dayOfMonth);
    }
    private void onOKButtonClick(DatePickerDialog datePickerDialog) {
        // Get the chosen date from the DatePickerDialog
        DatePicker datePicker = datePickerDialog.getDatePicker();
        int chosenYear = datePicker.getYear();
        int chosenMonth = datePicker.getMonth();
        int chosenDay = datePicker.getDayOfMonth();
        // Perform any action with the chosen date here
        // For example, you can pass it to a method or store it in SharedPreferences
        saveSelectedDate(chosenYear, chosenMonth, chosenDay);
        // Dismiss the dialog
        datePickerDialog.dismiss();
    }
    private void saveSelectedDate(int year, int month, int day) {
        // Obter o SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("selected_date", Context.MODE_PRIVATE);

        // Editar o SharedPreferences para adicionar a data selecionada
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("year", year);
        editor.putInt("month", month);
        editor.putInt("day", day);
        editor.apply();
    }
}