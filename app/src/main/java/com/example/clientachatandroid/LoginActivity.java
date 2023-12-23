package com.example.clientachatandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.clientachatandroid.databinding.LoginActivityBinding;

public class LoginActivity extends AppCompatActivity {
    private LoginActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
                String text = "username:" + binding.usernameInput.getText() + "\nmdp:" + binding.passwordInput.getText();
                Log.d("tag", text);
            }
        });

    }
}
