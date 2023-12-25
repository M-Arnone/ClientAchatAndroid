package com.example.clientachatandroid.network;

import android.content.Context;
import android.os.AsyncTask;
import com.example.clientachatandroid.model.Article;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class NetworkManager {

    private Context applicationContext;
    private Model m;


    public NetworkManager(Context applicationContext) throws SQLException, IOException, ClassNotFoundException {
        this.applicationContext = applicationContext;
        m = Model.getInstance(applicationContext);
    }

    public void performLoginAsync(String username, String password, OnLoginCompleteListener listener) {
        new LoginTask(listener).execute(username, password);
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {
        private final OnLoginCompleteListener listener;

        public LoginTask(OnLoginCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                m.on_pushLogin(params[0], params[1], false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Boolean.parseBoolean(params[2])

            // À la fin, appeler la méthode de l'interface lorsque l'opération est terminée
            if (listener != null) {
                listener.onLoginComplete();
            }

            return null;
        }
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
                return m.setArticle(params[0]);
            } catch (Exception e) {
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



    // Interface pour écouter la fin du login
    public interface OnLoginCompleteListener {
        void onLoginComplete();
    }
    public interface OnArticleFetchListener {
        void onArticleFetched(Article article);
        void onArticleFetchError(String errorMessage);
    }
}
