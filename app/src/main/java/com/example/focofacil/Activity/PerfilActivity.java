package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.focofacil.R;
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

public class PerfilActivity extends AppCompatActivity {
    private TextView txtNome, txtEmail, txtEditarNome, txtEditarSenha, txtEditarEmail;
    private ImageView imageFoto;
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

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        imageFoto = findViewById(R.id.imageFoto);
        txtEditarEmail = findViewById(R.id.txtEditarEmail);
        txtEditarNome = findViewById(R.id.txtEditarNome);
        txtEditarSenha = findViewById(R.id.txtEditarSenha);
        adView = findViewById(R.id.adView);

        //carrega e mostra o ad
        carregarAdIn();

        txtEditarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirecionar = new Intent(PerfilActivity.this, EditarNomeActivity.class);
                startActivity(redirecionar);
            }
        });

        txtEditarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirecionar = new Intent(PerfilActivity.this, EditarEmailActivity.class);
                startActivity(redirecionar);
            }
        });

        txtEditarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirecionar = new Intent(PerfilActivity.this, EditarSenhaActivity.class);
                startActivity(redirecionar);
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
    }

    //mostra o ad sempre que o app for minimizado e retornar
    @Override
    protected void onResume() {
        super.onResume();
        if(mInterstitialAd != null){
            carregarAdIn();
        }else{
            showInterstitial();
        }
    }

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