package com.example.focofacil.Utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.focofacil.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MenuCam {


    public static void showImagePickerMenu(final Fragment fragment) {
        // Verifica se a permissão da câmera já foi concedida
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permissão da câmera não concedida, solicita permissão
            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA_PERMISSION);
            return;
        }

        // Verifica se a permissão de armazenamento já foi concedida
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permissão de armazenamento não concedida, solicita permissão
            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_STORAGE_PERMISSION);
            return;
        }

        // Ambas as permissões já concedidas, infla o layout do menu
        View bottomSheetView = LayoutInflater.from(fragment.requireContext())
                .inflate(R.layout.bottom_sheet, null);

        // Cria o BottomSheetDialog
        final BottomSheetDialog dialog = new BottomSheetDialog(fragment.requireContext());
        dialog.setContentView(bottomSheetView);

        // Encontra os elementos do layout
        LinearLayout opCamera = bottomSheetView.findViewById(R.id.camera_option);
        LinearLayout opGaleria = bottomSheetView.findViewById(R.id.gallery_option);

        opCamera.setOnClickListener(v -> {
            // Abrir a câmera
            Intent redirecionar = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fragment.startActivityForResult(redirecionar, Constants.REQUEST_IMAGE_CAPTURE);
            dialog.dismiss();
        });

        opGaleria.setOnClickListener(v -> {
            // Abrir a galeria
            Intent redirecionar = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            redirecionar.setType("image/*");
            fragment.startActivityForResult(Intent.createChooser(redirecionar, "Selecione uma imagem"), Constants.REQUEST_IMAGE_PICK);
            dialog.dismiss();
        });

        // Mostra o menu
        dialog.show();
    }
}
