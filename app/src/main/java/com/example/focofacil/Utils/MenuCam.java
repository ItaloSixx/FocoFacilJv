package com.example.focofacil.Utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.focofacil.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MenuCam {

    public static void showImagePickerMenu(final Fragment fragment) {

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
                //abrir a galeria
                Intent redirecionar = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                redirecionar.setType("image/*");
                fragment.startActivityForResult(Intent.createChooser(redirecionar, "Selecione uma imagem"), Constants.REQUEST_IMAGE_PICK);
                dialog.dismiss();
            });

            // Mostra o menu
            dialog.show();


    }


    public static void permissaoNecessaria(Fragment fragment) {
        View dialogView = LayoutInflater.from(fragment.requireContext()).inflate(R.layout.permissaodialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.requireContext());
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();


        Button Conceder = dialogView.findViewById(R.id.Conceder);
        Conceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + fragment.requireContext().getPackageName()));
                fragment.startActivity(intent);
            }
        });

        Button Cancelar = dialogView.findViewById(R.id.Cancelar);
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void solicitarPermissao(Fragment fragment){
        if(ActivityCompat.checkSelfPermission(fragment.requireActivity(), Constants.REQUEST_CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED){
            showImagePickerMenu(fragment);
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(fragment.requireActivity(), Constants.REQUEST_CAMERA_PERMISSION)){
            permissaoNecessaria(fragment);
        }else{
            ActivityCompat.requestPermissions(fragment.requireActivity(), new String[]{Constants.REQUEST_CAMERA_PERMISSION}, Constants.REQUEST_CAMERA_CODE);
        }

    }




}
