package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private static ViewModelFactory instance;

    private ViewModelFactory() {
        // Construtor privado para garantir que apenas uma instância seja criada
    }

    public static synchronized ViewModelFactory getInstance() {
        if (instance == null) {
            instance = new ViewModelFactory();
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ActivityCadastrarDiaViewModel.class)) {
            return (T) new ActivityCadastrarDiaViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}