package com.example.clientachatandroid.network;
import android.content.Context;
import android.os.AsyncTask;
import com.example.clientachatandroid.model.Article;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class ArticleManager {

    private Model model;

    public ArticleManager(Context context) throws SQLException, IOException, ClassNotFoundException {
        this.model = Model.getInstance(context);
    }

    public void fetchArticleAsync(int articleId, OnArticleFetchListener listener) {
        new FetchArticleTask(listener).execute(articleId);
    }

    private class FetchArticleTask extends AsyncTask<Integer, Void, Article> {
        private final OnArticleFetchListener listener;

        public FetchArticleTask(OnArticleFetchListener listener) {
            this.listener = listener;
        }

        @Override
        protected Article doInBackground(Integer... params) {
            try {
                // Effectuez vos opérations réseau ici (ex. récupérer l'article du serveur)
                return model.setArticle(params[0]);
            } catch (Exception e) {
                // Gérer l'erreur de manière appropriée (affichage à l'utilisateur, journalisation, etc.)
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Article article) {
            super.onPostExecute(article);
            if (article != null) {
                // Appeler la méthode de l'interface lorsque l'opération est terminée avec succès
                listener.onArticleFetched(article);
            } else {
                // Appeler la méthode de l'interface en cas d'erreur
                listener.onArticleFetchError("Erreur lors de la récupération de l'article");
            }
        }
    }

    // Interface pour écouter la fin de la récupération d'article
    public interface OnArticleFetchListener {
        void onArticleFetched(Article article);
        void onArticleFetchError(String errorMessage);
    }
}
