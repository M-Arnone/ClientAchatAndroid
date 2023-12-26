package com.example.clientachatandroid.activities.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.example.clientachatandroid.activities.CartActivity;
import com.example.clientachatandroid.activities.MenuActivity;
import com.example.clientachatandroid.R;
import com.example.clientachatandroid.model.Article;
import com.example.clientachatandroid.model.Model;
import com.example.clientachatandroid.network.ArticleAcheterManager;
import com.example.clientachatandroid.network.ArticleManager;
import com.google.android.material.snackbar.Snackbar;
import com.example.clientachatandroid.databinding.ActivityMainBinding;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MenuActivity implements ArticleManager.OnArticleFetchListener {
    private ActivityMainBinding binding;
    Model m;
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
        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        suivantButton.setOnClickListener(v -> {
            // Incrémentez l'ID de l'article et fetch l'article suivant
            if (m.getNumArticle() < 21) {
                m.setNumArticle(m.getNumArticle() + 1);
                articleManager.fetchArticleAsync(m.getNumArticle(), MainActivity.this);
            } else {
                Snackbar.make(v, "Vous êtes sur le dernier article", Snackbar.LENGTH_SHORT).show();
            }

        });

        precedentButton.setOnClickListener(v -> {
            if (m.getNumArticle() > 1) {
                m.setNumArticle(m.getNumArticle() - 1);
                articleManager.fetchArticleAsync(m.getNumArticle(), MainActivity.this);
            } else {
                Snackbar.make(v, "Vous êtes déjà sur le premier article", Snackbar.LENGTH_SHORT).show();
            }
        });
        try {
            ArticleAcheterManager articleAcheterManager = new ArticleAcheterManager(getApplicationContext());
            acheterButton.setOnClickListener(v -> {
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
            });
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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

        String nomImage = nomArticle.toLowerCase();
        if (nomImage.equals("pommes de terre"))
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