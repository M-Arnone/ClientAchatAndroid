package com.example.clientachatandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.clientachatandroid.databinding.LoginActivityBinding;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    Model m;
    private LoginActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            m = Model.getInstance(getApplicationContext());
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new LoginTask().execute(binding.usernameInput.getText().toString(), binding.passwordInput.getText().toString());
            }
        });

    }
    private class LoginTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                m.on_pushLogin(params[0], params[1], false);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Rediriger vers l'activité principale après la connexion réussie
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
