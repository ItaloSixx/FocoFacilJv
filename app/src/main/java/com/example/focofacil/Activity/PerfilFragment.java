package com.example.focofacil.Activity;

import static androidx.core.app.ActivityCompat.finishAffinity;
import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.focofacil.Bd.ConfigureBd;
import com.example.focofacil.R;
import com.example.focofacil.Utils.Constants;
import com.example.focofacil.Utils.MenuCam;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView txtNomePerfil, txtEmailPerfil, txtSenhaPerfil, txtNomeDestaque;
    ImageView imgPerfil, imgEditar;
    FirebaseDatabase database;
    Button btnExcluir;


    public PerfilFragment() {
        // Required empty public constructor
    }


    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        txtNomePerfil = view.findViewById(R.id.txtNomePerfil);
        txtEmailPerfil = view.findViewById(R.id.txtEmailPerfil);
        txtSenhaPerfil = view.findViewById(R.id.txtSenhaPerfil);
        txtNomeDestaque = view.findViewById(R.id.txtNomeDestaque);
        imgPerfil = view.findViewById(R.id.imgPerfil);
        imgEditar = view.findViewById(R.id.imgEditar);

        database = FirebaseDatabase.getInstance();

        txtEmailPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditarEmailActivity.class);
            startActivity(intent);
        });

        txtNomePerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditarNomeActivity.class);
            startActivity(intent);
        });

        txtSenhaPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditarSenhaActivity.class);
            startActivity(intent);
        });

        imgEditar.setOnClickListener(v -> {
            MenuCam.solicitarPermissao(this);
        });

        mostrarPerfil();


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }



    public void mostrarPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String nome = user.getDisplayName();
            String email = user.getEmail();
            txtNomePerfil.setText(nome);
            txtEmailPerfil.setText(email);
            txtNomeDestaque.setText(nome);

            Uri fotoUrl = user.getPhotoUrl();
            if(fotoUrl != null){
                Glide.with(this).load(fotoUrl).fitCenter().into(imgPerfil);
            }else{
                imgPerfil.setImageResource(R.drawable.fotopadrao);
            }
        }
    }





    @SuppressLint("RestrictedApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ConfigureBd.FirebaseAutenticar();

        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
            //pega a imagem da camera
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                //converte bitmap para URI
                Uri imagemSelecionada = getImageUri(getActivity(), imageBitmap);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null && imagemSelecionada != null) {
                    user.updateProfile(new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(imagemSelecionada)
                                    .build())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        updateProfileImageUrl(user.getUid(), user.getPhotoUrl().toString());
                                        Toast.makeText(getActivity(), "Imagem do perfil alterada", Toast.LENGTH_SHORT).show();
                                        mostrarPerfil();
                                    } else {
                                        Log.d(TAG, "Erro ao alterar imagem do perfil" + task.getException().getMessage());
                                    }
                                }
                            });
                }
            }
        } else if (requestCode == Constants.REQUEST_IMAGE_PICK) {
            //seleciona a imagem na galeria
            Uri imagemSelecionada = data.getData();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && imagemSelecionada != null) {
                //salva a imagem selecionada
                Uri imageUri = saveImageToGallery(getActivity(), imagemSelecionada, "Titulo");

                if (imageUri != null) {
                    user.updateProfile(new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(imageUri)
                                    .build())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @SuppressLint("RestrictedApi")
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        updateProfileImageUrl(user.getUid(), user.getPhotoUrl().toString());
                                        Toast.makeText(getActivity(), "Imagem do perfil alterada", Toast.LENGTH_SHORT).show();
                                        mostrarPerfil();
                                    } else {
                                        Log.d(TAG, "Erro ao alterar imagem do perfil" + task.getException().getMessage());
                                    }
                                }
                            });
                } else {
                    Log.d(TAG, "Falha ao salvar a imagem na galeria");
                }
            }
        }


    }

    public static void updateProfileImageUrl(String userId, String imageUrl) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        userRef.child("fotoPerfil").setValue(imageUrl);
    }

    //converte a imagem capturada pela câmera em jpeg
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Titulo", null);
        return Uri.parse(path);
    }

    //esse metodo foi necessário para adequar a imagem a ser mostradas em todas imageView, sem ele, dependendo do tamanho do imageView a imagem ficava em branco
    private Uri saveImageToGallery(Context context, Uri imageUri, String title) {
        try {
            //carrega a imagem original da galeria
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            //cria um novo arquivo temporário para salvar a imagem
            File tempFile = File.createTempFile("temp_image", ".jpg", context.getExternalCacheDir());
            //abre um OutputStream para escrever a imagem no arquivo temporário
            FileOutputStream fos = new FileOutputStream(tempFile);
            //comprime a imagem para o formato JPEG com qualidade 100
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            //fecha o OutputStream
            fos.close();
            //insere a imagem no armazenamento e obtém o URI do arquivo
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), tempFile.getAbsolutePath(), title, null);

            return Uri.parse(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.REQUEST_CAMERA_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                MenuCam.showImagePickerMenu(this);
            }
        }else if(!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Constants.REQUEST_CAMERA_PERMISSION)){
                MenuCam.permissaoNecessaria(this);
        }else{
            MenuCam.solicitarPermissao(this);
        }
    }

}
