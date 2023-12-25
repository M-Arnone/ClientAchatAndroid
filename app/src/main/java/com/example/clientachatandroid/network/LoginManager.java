package com.example.clientachatandroid.network;
import android.content.Context;
import android.os.AsyncTask;
import com.example.clientachatandroid.model.Model;
import java.io.IOException;
import java.sql.SQLException;
public class LoginManager {

    private Context context;
    private Model model;

    public LoginManager(Context context) throws SQLException, IOException, ClassNotFoundException {
        this.context = context;
        this.model = Model.getInstance(context);
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
                model.on_pushLogin(params[0], params[1], false);
            } catch (IOException e) {
                // Gérer l'erreur de manière appropriée (affichage à l'utilisateur, journalisation, etc.)
                e.printStackTrace();
            }

            // À la fin, appeler la méthode de l'interface lorsque l'opération est terminée
            if (listener != null) {
                listener.onLoginComplete();
            }

            return null;
        }
    }

    // Interface pour écouter la fin du login
    public interface OnLoginCompleteListener {
        void onLoginComplete();
    }
}
