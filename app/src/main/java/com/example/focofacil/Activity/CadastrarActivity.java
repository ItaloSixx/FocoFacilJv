package com.example.focofacil.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.focofacil.Bd.ConfigureBd;
import com.example.focofacil.R;
import com.example.focofacil.Model.User;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CadastrarActivity extends AppCompatActivity {

    EditText edtNome, edtEmail, edtSenha, edtSenhaConfirm;
    Button btnCadastrar, btnLoginGoogle;
    TextView txtLoginTela;
    boolean senhaVer;
    FirebaseAuth auth;
    GoogleSignInClient client;
    FirebaseDatabase database;
    int RC_SIGN_IN = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtSenhaConfirm = findViewById(R.id.edtSenhaConfirm);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnLoginGoogle = findViewById(R.id.btnLogGoogle);
        txtLoginTela = findViewById(R.id.txtLoginTela);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nome = edtNome.getText().toString();
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                String senhaConfirm = edtSenhaConfirm.getText().toString();

                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senhaConfirm.isEmpty()) {
                    Toast.makeText(CadastrarActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (senha.equals(senhaConfirm)) {
                    User user = new User(nome, email, senha);
                    cadastrarUsuario(user);
                } else {
                    Toast.makeText(CadastrarActivity.this, "Senhas não correspondem", Toast.LENGTH_SHORT).show();
                }
            }
        });


        txtLoginTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirecionar = new Intent(CadastrarActivity.this, LoginActivity.class);
                startActivity(redirecionar);
            }
        });
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

        senhaVer = false;
        edtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //senha visivel/nao visivel
        edtSenha.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if(event.getAction()==MotionEvent.ACTION_UP){
                if(event.getRawX()>=edtSenha.getRight()-edtSenha.getCompoundDrawables()[Right].getBounds().width()){
                    int selecao = edtSenha.getSelectionEnd();
                    if(senhaVer){
                        edtSenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeoff, 0);
                        edtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        senhaVer = false;
                    }else{
                        edtSenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeon, 0);
                        edtSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        senhaVer = true;
                    }
                    edtSenha.setSelection(selecao);
                    return true;
                }
            }
            return false;
        });

        edtSenhaConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        //senha visivel/nao visivel
        edtSenhaConfirm.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if(event.getAction()==MotionEvent.ACTION_UP){
                if(event.getRawX()>=edtSenhaConfirm.getRight()-edtSenhaConfirm.getCompoundDrawables()[Right].getBounds().width()){
                    int selecao = edtSenhaConfirm.getSelectionEnd();
                    if(senhaVer){
                        edtSenhaConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeoff, 0);
                        edtSenhaConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        senhaVer = false;
                    }else{
                        edtSenhaConfirm.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eyeon, 0);
                        edtSenhaConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        senhaVer = true;
                    }
                    edtSenhaConfirm.setSelection(selecao);
                    return true;
                }
            }
            return false;
        });
    }

    public void cadastrarUsuario(User user) {
        //tela carregamento
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();

        ConfigureBd.FirebaseAutenticar();
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getSenhaHashed())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            //definir nome do usuario
                            if (firebaseUser != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(user.getNome())
                                        .build();

                                firebaseUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> updateProfileTask) {
                                                if (updateProfileTask.isSuccessful()) {
                                                    //definido o nome
                                                    firebaseUser.sendEmailVerification()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> emailVerificationTask) {
                                                                    if (emailVerificationTask.isSuccessful()) {
                                                                        HashMap<String, Object> userMap = new HashMap<>();
                                                                        userMap.put("nome", user.getNome());
                                                                        userMap.put("email", user.getEmail());
                                                                        FirebaseDatabase.getInstance().getReference("User")
                                                                                .child(firebaseUser.getUid())
                                                                                .setValue(userMap)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> dbUpdateTask) {
                                                                                        if (dbUpdateTask.isSuccessful()) {
                                                                                            Intent redirecionar = new Intent(CadastrarActivity.this, VerificarEmailActivity.class);
                                                                                            startActivity(redirecionar);
                                                                                            finish();
                                                                                        } else {
                                                                                            Toast.makeText(CadastrarActivity.this, "Falha ao cadastrar usuário" +
                                                                                                    dbUpdateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });
                                                                    } else {
                                                                        Toast.makeText(CadastrarActivity.this, "Falha ao enviar email de verificação" +
                                                                                emailVerificationTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    //falha ao definir o nome do usuário
                                                    Toast.makeText(CadastrarActivity.this, "Falha ao definir o nome do usuário: " +
                                                            updateProfileTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(CadastrarActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CadastrarActivity.this, "Falha ao cadastrar usuário: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        if(user != null) {
            //Intent redirecionar = new Intent(CadastrarActivity.this, CadastrarActivity.class);
            //startActivity(redirecionar);
        }
    }

    private void firebaseAuthGoogle(String idToken) {
        //tela carregamento
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();

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
                            Intent redirecionar = new Intent(getApplicationContext(), PerfilActivity.class);
                            startActivity(redirecionar);
                        }else{
                            Toast.makeText(CadastrarActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
