package com.example.clientachatandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.clientachatandroid.model.Article;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private List<Article> panier = new ArrayList<>();
    private PanierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        try {
            panier = Model.getInstance(null).getPanier();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        // Add articles to your cart as needed
//        panier.add(new Article(1, "Article 1", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));
//        panier.add(new Article(2, "Article 2", 29.99, 2));

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new PanierAdapter(panier, new PanierAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Vous pouvez gérer le clic sur un élément ici si nécessaire
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Button to clear the cart
        Button clearCartButton = findViewById(R.id.clearCartButton);
        clearCartButton.setOnClickListener(view -> clearCart());

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            changeLanguage();
            return true;
        } else if (id==android.R.id.home) {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage() {
        // Obtenez l'objet Resources pour accéder aux ressources de l'application
        Resources resources = getResources();

        // Obtenir la configuration actuelle
        Configuration configuration = resources.getConfiguration();

        // Changer la locale en français si elle est actuellement en anglais, et vice versa
        if (configuration.locale.getLanguage().equals("en")) {
            setLocale("fr");
        } else {
            setLocale("en");
        }

        // Rafraîchir l'affichage de l'activité actuelle
        recreate();
    }

    private void setLocale(String languageCode) {
        // Changer la locale de l'application
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Enregistrer la langue sélectionnée dans les préférences partagées (si nécessaire)
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("language", languageCode);
        editor.apply();
    }

    private void removeItem(int position) {
        panier.remove(position);
        adapter.notifyItemRemoved(position);
    }

    private void clearCart() {
        panier.clear();
        adapter.notifyDataSetChanged();
    }
}
