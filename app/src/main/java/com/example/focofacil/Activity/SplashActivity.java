package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.focofacil.R;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_ALL_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        solicitarTodasAsPermissoes();

    }

    private void solicitarTodasAsPermissoes() {
        //verifica se o dispositivo está executando o Android 6.0 ou superior,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            };
            ActivityCompat.requestPermissions(this, permissions, REQUEST_ALL_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ALL_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Algumas funcionalidades podem não funcionar", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }
}