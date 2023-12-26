package com.example.clientachatandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.clientachatandroid.activities.main.MainActivity;
import com.example.clientachatandroid.databinding.LoginActivityBinding;
import com.example.clientachatandroid.model.Model;
import com.example.clientachatandroid.network.LoginManager;

import java.io.IOException;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    Model m;
    private LoginActivityBinding binding;
    LoginManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            m = Model.getInstance(getApplicationContext());
            lm = new LoginManager(getApplicationContext());
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        binding.btnLogin.setOnClickListener(view -> lm.performLoginAsync(
//                        binding.usernameInput.getText().toString(),
//                        binding.passwordInput.getText().toString(),
                "w",
                "1",
                () -> {
                    // Rediriger vers l'activité principale après la connexion réussie
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
        ));

    }

}
