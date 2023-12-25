package com.example.clientachatandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import com.example.clientachatandroid.model.Article;
import com.example.clientachatandroid.model.Model;
import com.example.clientachatandroid.network.ArticleAcheterManager;
import com.example.clientachatandroid.network.ArticleManager;
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
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ArticleManager.OnArticleFetchListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Article a;
    Model m ;
    ArticleManager articleManager;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        Button suivantButton = findViewById(R.id.suivantButton);
        Button precedentButton = findViewById(R.id.precedentButton);
        Button acheterButton = findViewById(R.id.acheterButton);
        try {
            m = Model.getInstance(getApplicationContext());
            articleManager = new ArticleManager(getApplicationContext());
            articleManager.fetchArticleAsync(m.getNumArticle(), this);

        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        suivantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Incrémentez l'ID de l'article et fetch l'article suivant
                if(m.getNumArticle() <= 21) {
                    m.setNumArticle(m.getNumArticle()+1);
                    articleManager.fetchArticleAsync(m.getNumArticle(), MainActivity.this);
                }
                else{
                    Snackbar.make(v, "Vous êtes sur le dernier article", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        precedentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m.getNumArticle() > 1) {
                    m.setNumArticle(m.getNumArticle()-1);
                    articleManager.fetchArticleAsync(m.getNumArticle(), MainActivity.this);
                } else {
                    Snackbar.make(v, "Vous êtes déjà sur le premier article", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        try {
            ArticleAcheterManager articleAcheterManager = new ArticleAcheterManager(getApplicationContext());
            acheterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Spinner quantiteSpinner = findViewById(R.id.quantiteSpinner);
                    int quantite = (int) quantiteSpinner.getSelectedItem();

                    articleAcheterManager.acheterArticleAsync(quantite, new ArticleAcheterManager.OnAchatListener() {
                        @Override
                        public void onAchatSuccess() {
                            Toast.makeText(MainActivity.this, "Article acheté", Toast.LENGTH_SHORT).show();
                            articleManager.fetchArticleAsync(m.getNumArticle(), MainActivity.this);
                        }

                        @Override
                        public void onAchatError(String errorMessage) {
                            // Gérer l'erreur d'achat (affichage à l'utilisateur, journalisation, etc.)
                        }
                    });
                }
            });
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


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
        System.out.println("Nom de l'image :" + nomImage);
        if(nomImage.equals("pommes de terre") )
            nomImage = "pommesdeterre";
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