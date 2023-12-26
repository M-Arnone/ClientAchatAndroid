package com.example.clientachatandroid.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clientachatandroid.R;
import com.example.clientachatandroid.activities.main.MainActivity;
import com.example.clientachatandroid.network.LogoutManager;
import com.example.clientachatandroid.util.Language;

import java.io.IOException;
import java.sql.SQLException;

public class MenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            handleMenuLangue();
            return true;
        } else if (id == R.id.action_logout) {
            handleMenuLogout();
            return true;
        }
        if (id == android.R.id.home) {
            handleMenuBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuLangue() {
        Language.changeLanguage(this);
        recreate();
    }

    protected void handleMenuBack() {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    protected void handleMenuLogout() {
        try {
            new LogoutManager().performLogoutAsync(() -> {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

