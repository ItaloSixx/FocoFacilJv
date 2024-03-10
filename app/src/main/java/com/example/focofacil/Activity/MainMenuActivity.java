package com.example.focofacil.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.focofacil.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    TextView txtNome, txtEmail;
    ImageView fotoPerfil;
    Button btnPerfil;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);

        View headerView = navigationView.getHeaderView(0);
        fotoPerfil = headerView.findViewById(R.id.fotoPerfilNav);
        txtNome = headerView.findViewById(R.id.txtNomeNav);
        txtEmail = headerView.findViewById(R.id.txtEmailNav);
        btnPerfil = headerView.findViewById(R.id.nav_perfil);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (navigationView != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PerfilFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_perfil);
        }

        mostrarPerfil();

        //substitui o fragmento atual, sempre vai começar nesse quando chamar a MainMenuActivity
        replaceFragment(new HomeFragment());
        //replaceFragment(new PerfilFragment());
        //ad
        carregarAdIn();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.ClipBoard) {
                replaceFragment(new CadastrarDiaFragment());
            } else if (itemId == R.id.Perfil) {
                replaceFragment(new PerfilFragment());
            }
            return true;
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void mostrarPerfil() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String nome = user.getDisplayName();
            String email = user.getEmail();
            txtNome.setText(nome);
            txtEmail.setText(email);

            Uri fotoUrl = user.getPhotoUrl();
            if(fotoUrl != null){
                Glide.with(this).load(fotoUrl).fitCenter().into(fotoPerfil);
            }else{
                fotoPerfil.setImageResource(R.drawable.fotopadrao);
            }
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_perfil) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PerfilFragment()).commit();
        } else if (itemId == R.id.nav_settings) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).commit();
        } else if (itemId == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Deslogado!", Toast.LENGTH_SHORT).show();
            Intent redirecionar = new Intent(MainMenuActivity.this, LoginActivity.class);
            startActivity(redirecionar);
            finishAffinity();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(MainMenuActivity.this);
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
