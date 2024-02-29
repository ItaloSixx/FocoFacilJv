package com.example.focofacil.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.focofacil.R;

public class fragment_test extends Fragment {

    private EditText editText1;
    private EditText editText2;
    private Button button;

    public fragment_test() {
    }

    public static fragment_test newInstance() {
        return new fragment_test();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = editText1.getText().toString();
                String text2 = editText2.getText().toString();

                Toast.makeText(getActivity(), "Texto 1: " + text1 + "\nTexto 2: " + text2, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
