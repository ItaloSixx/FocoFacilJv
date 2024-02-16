package com.example.focofacil;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {

    public static String generateSecurePassword(String password) {
        try {
            // Gera um "salt" aleatório
            byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(32);

            // Cria um "key" usando o algoritmo PBKDF2WithHmacSHA1
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey key = factory.generateSecret(spec);

            // Converte o "key" em um hash com Base64
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyPassword(String password, String passwordHash) {
        try {
            // Extrai o "salt" do hash
            byte[] salt = Base64.getDecoder().decode(passwordHash.split(":")[1]);

            // Recria o "key" usando o "salt" e a senha digitada
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey key = factory.generateSecret(spec);

            // Gera um novo hash com a senha digitada e compara com o hash armazenado
            String newHash = Base64.getEncoder().encodeToString(key.getEncoded());
            return passwordHash.equals(newHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }
}
