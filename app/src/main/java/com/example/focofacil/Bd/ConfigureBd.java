package com.example.focofacil.Bd;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigureBd {
    private static FirebaseAuth auth;

    public static FirebaseAuth FirebaseAutenticar(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
    public static void FirebasePersistirOffline() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public Task<Boolean> taCadastrado(String email) {
        FirebaseAutenticar();
        return auth.signInWithEmailAndPassword(email, "senhazinha")
                .continueWith(new Continuation<AuthResult, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<AuthResult> task) throws Exception {
                        if (task.isSuccessful()) {
                            //Usuário tá registrado
                            return true;
                        } else {
                            String errorMessage = task.getException().getMessage();
                            if (errorMessage.contains("user-not-found")) {
                                //Usuário não tá registrado
                                return false;
                            } else {
                                //Lançar uma exceção para indicar outros erros
                                throw task.getException();
                            }
                        }
                    }
                });
    }
}
