package com.example.clientachatandroid.network;

import android.content.Context;
import android.os.AsyncTask;
import com.example.clientachatandroid.model.Article;
import com.example.clientachatandroid.model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class ArticleAcheterManager {

    private Model model;

    public ArticleAcheterManager(Context context) throws IOException, SQLException, ClassNotFoundException {
        this.model = Model.getInstance(context);
    }

    public void acheterArticleAsync(int quantite, OnAchatListener listener) {
        new AchatArticleTask(listener).execute(quantite);
    }

    private class AchatArticleTask extends AsyncTask<Integer, Void, Boolean> {
        private final OnAchatListener listener;

        public AchatArticleTask(OnAchatListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                // Effectuez vos opérations réseau ici (ex. acheter l'article du serveur)
                int quantite = params[0];
                model.on_pushAcheter(quantite);
                return true;  // L'achat a réussi
            } catch (IOException e) {
                // Gérer l'erreur de manière appropriée (affichage à l'utilisateur, journalisation, etc.)
                e.printStackTrace();
                return false;  // L'achat a échoué
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                // Appeler la méthode de l'interface lorsque l'opération est terminée avec succès
                listener.onAchatSuccess();
            } else {
                // Appeler la méthode de l'interface en cas d'erreur
                listener.onAchatError("Erreur lors de l'achat de l'article");
            }
        }
    }

    // Interface pour écouter la fin de l'achat d'article
    public interface OnAchatListener {
        void onAchatSuccess();
        void onAchatError(String errorMessage);
    }
}
