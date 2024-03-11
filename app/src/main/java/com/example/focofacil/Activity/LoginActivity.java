package com.example.focofacil.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.focofacil.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnLoginGoogle;
    TextView txtCadTela, txtEsqueceuSenha;
    EditText edtEmail, edtSenha;
    boolean senhaVer;
    GoogleSignInClient client;
    FirebaseDatabase database;
    FirebaseAuth auth;
    int RC_SIGN_IN = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGoogle = findViewById(R.id.btnLogGoogle);
        txtCadTela = findViewById(R.id.txtCadTela);
        txtEsqueceuSenha = findViewById(R.id.txtEsqueceuSenha);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this,gso);

        btnLoginGoogle.setOnClickListener(v -> {
            googleSingIn();
        });

        txtCadTela.setOnClickListener(v -> {
            Intent redirecionar = new Intent(LoginActivity.this, CadastrarActivity.class);
            startActivity(redirecionar);
        });

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();
            fazerLogin(email, senha);
        });

        txtEsqueceuSenha.setOnClickListener(v -> {
            Intent redirecionar = new Intent(this, RecuperarSenhaActivity.class);
            startActivity(redirecionar);
        });

        senhaVer = false;
        edtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //senha visivel/nao visivel
        edtSenha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=edtSenha.getRight()-edtSenha.getCompoundDrawables()[Right].getBounds().width()){
                        int selecao = edtSenha.getSelectionEnd();
                        if(senhaVer){
                            edtSenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeon, 0);
                            edtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            senhaVer = false;
                        }else{
                            edtSenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeoff, 0);
                            edtSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            senhaVer = true;
                        }
                        edtSenha.setSelection(selecao);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void fazerLogin(String email, String senha) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if(user != null && user.isEmailVerified()) {
                                // Tela de carregamento
                                final LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
                                loadingDialog.startLoadingDialog();
                                Toast.makeText(LoginActivity.this, "Logado", Toast.LENGTH_SHORT).show();
                                Intent redirecionar = new Intent(LoginActivity.this, MainMenuActivity.class);
                                startActivity(redirecionar);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Por favor verifique o seu email", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Falha ao logar:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void googleSingIn(){
        Intent redirecionar = client.getSignInIntent();
        startActivityForResult(redirecionar, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account =  task.getResult(ApiException.class);
                firebaseAuthGoogle(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && (user.isEmailVerified() || logadoGoogle(user))) {
            Intent redirecionar = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(redirecionar);
        }
    }
    //login com google
    private void firebaseAuthGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("nome", user.getDisplayName());
                            map.put("email", user.getEmail());
                            if (user.getPhotoUrl() != null) {
                                map.put("fotoPerfil", user.getPhotoUrl().toString());
                            }
                            database.getReference().child("User").child(user.getUid()).setValue(map);
                            Intent redirecionar = new Intent(getApplicationContext(), MainMenuActivity.class);
                            startActivity(redirecionar);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean logadoGoogle(FirebaseUser user){
        List<UserInfo> provedor = (List<UserInfo>) user.getProviderData();
        for(UserInfo userInfo : provedor){
            if(userInfo.getProviderId().equals("google.com")){
                return true;
            }
        }
        return false;
    }
}
