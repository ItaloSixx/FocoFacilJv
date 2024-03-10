package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.focofacil.R;
import com.example.focofacil.Utils.MenuCam;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PerfilActivity extends AppCompatActivity {
    private TextView txtNome, txtEmail, txtEditarNome, txtEditarSenha, txtEditarEmail, txtCadTest;
    private ImageView imageFoto, imgEditar;
    private InterstitialAd mInterstitialAd;
    private static final String TAG = "PerfilActivity";
    private AdView adView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Inicialização do SDK do adMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        txtNome = findViewById(R.id.txtNomeNav);
        txtEmail = findViewById(R.id.txtEmailNav);
        imageFoto = findViewById(R.id.imageFoto);
        txtEditarEmail = findViewById(R.id.txtEditarEmail);
        txtEditarNome = findViewById(R.id.txtEditarNome);
        txtEditarSenha = findViewById(R.id.txtEditarSenha);
        adView = findViewById(R.id.adView);
        txtCadTest = findViewById(R.id.cadTarefaTest);
        imgEditar = findViewById(R.id.imgEditar);

        //qcarrega e mostra o ad
        carregarAdIn();

        txtCadTest.setOnClickListener(v -> {
            Intent redirecionar = new Intent(PerfilActivity.this, MainMenuActivity.class);
            startActivity(redirecionar);
        });

        txtEditarNome.setOnClickListener(v -> {
            Intent redirecionar = new Intent(PerfilActivity.this, EditarNomeActivity.class);
            startActivity(redirecionar);
        });

        txtEditarEmail.setOnClickListener(v -> {
            Intent redirecionar = new Intent(PerfilActivity.this, EditarEmailActivity.class);
            startActivity(redirecionar);
        });

        txtEditarSenha.setOnClickListener(v -> {
            Intent redirecionar = new Intent(PerfilActivity.this, EditarSenhaActivity.class);
            startActivity(redirecionar);
        });

        imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // MenuCam.showImagePickerMenu(PerfilActivity.this);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String nome = user.getDisplayName();
            String email = user.getEmail();
            txtNome.setText(nome);
            txtEmail.setText(email);

            Uri fotoUrl = user.getPhotoUrl();
            if (fotoUrl != null) {
                Glide.with(this).load(fotoUrl).into(imageFoto);
            } else {
                imageFoto.setImageResource(R.drawable.img_group142);
            }
        }

        if(user!=null){
            for (UserInfo profile : user.getProviderData()) {
                String providerId = profile.getProviderId();

                String uid = profile.getUid();

                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
            }
        }
    }

    //mostra o ad sempre que o app for minimizado e retornar
    //@Override
    //protected void onResume() {
      //  super.onResume();
        //if(mInterstitialAd != null){
     //       carregarAdIn();
        //}else{
          //  showInterstitial();
        //}
    //}

    private void showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(PerfilActivity.this);
        } else {
            Log.d(TAG, "Anúncio intersticial não carregado ainda.");
        }
    }

    private void carregarAdIn() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onLoaded");
                        if (mInterstitialAd != null) {
                            showInterstitial();
                        } else {
                            Log.d(TAG, "Anúncio intersticial não foi carregado com sucesso.");
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }





}