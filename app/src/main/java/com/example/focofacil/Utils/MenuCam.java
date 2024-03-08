package com.example.focofacil.Utils;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import com.example.focofacil.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MenuCam {
    public static void showImagePickerMenu(final Fragment fragment) {
        //infla o layout do menu
        View bottomSheetView = LayoutInflater.from(fragment.requireContext())
                .inflate(R.layout.bottom_sheet_image_picker, null);

        //cria o BottomSheetDialog
        final BottomSheetDialog dialog = new BottomSheetDialog(fragment.requireContext());
        dialog.setContentView(bottomSheetView);

        //encontra os elementos do layout
        LinearLayout opCamera = bottomSheetView.findViewById(R.id.camera_option);
        LinearLayout opGaleria = bottomSheetView.findViewById(R.id.gallery_option);

        opCamera.setOnClickListener(v -> {
            //abrir camera
            Intent redirecionar = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fragment.startActivityForResult(redirecionar, Constants.REQUEST_IMAGE_CAPTURE);
            dialog.dismiss();
        });

        opGaleria.setOnClickListener(v -> {
            //abrir galeria
            Intent redirecionar = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            redirecionar.setType("image/*");
            fragment.startActivityForResult(Intent.createChooser(redirecionar, "Selecione uma imagem"), Constants.REQUEST_IMAGE_PICK);
            dialog.dismiss();
        });

        //mostra o menu
        dialog.show();
    }
}
