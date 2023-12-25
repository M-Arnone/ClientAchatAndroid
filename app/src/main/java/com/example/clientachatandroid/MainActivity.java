package com.example.clientachatandroid;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.clientachatandroid.model.Article;
import com.example.clientachatandroid.model.Model;
import com.example.clientachatandroid.network.NetworkManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.clientachatandroid.databinding.ActivityMainBinding;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import android.view.Menu;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NetworkManager.OnArticleFetchListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Article a;
    Model m;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            NetworkManager networkManager = new NetworkManager(getApplicationContext());
            networkManager.fetchArticleAsync(1, this);

        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onArticleFetched(Article article) {
        updateUI(article);
    }

    @Override
    public void onArticleFetchError(String errorMessage) {
        Snackbar.make(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG).show();
    }

    private void updateUI(Article article) {
        ImageView articleImageView = findViewById(R.id.articleImageView);
        TextView nomArticleTextView = findViewById(R.id.nomArticleTextView);
        TextView prixUnitaireTextView = findViewById(R.id.prixUnitaireTextView);
        TextView stockTextView = findViewById(R.id.stockTextView);

        nomArticleTextView.setText(article.getNom());
        prixUnitaireTextView.setText(String.format("Prix : %.2f €", article.getPrix()));
        stockTextView.setText(String.format("Stock : %d unités", article.getQuantite()));

        String nomArticle = article.getNom();

        String nomImage = nomArticle.toLowerCase(); // Assurez-vous que nomArticle ne contient pas d'espaces ni de caractères spéciaux
        int imageResource = getResources().getIdentifier(nomImage, "drawable", getPackageName());
        articleImageView.setImageResource(imageResource);

        Spinner quantiteSpinner = findViewById(R.id.quantiteSpinner);

        // Mettre à jour la liste des quantités possibles dans le Spinner
        List<Integer> quantiteList = new ArrayList<>();
        for (int i = 1; i <= article.getQuantite(); i++) {
            quantiteList.add(i);
        }

        ArrayAdapter<Integer> quantiteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quantiteList);
        quantiteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantiteSpinner.setAdapter(quantiteAdapter);

        // Mettre à jour la sélection du Spinner en fonction de la quantité actuelle de l'article
        quantiteSpinner.setSelection(article.getQuantite() - 1);




    }
}